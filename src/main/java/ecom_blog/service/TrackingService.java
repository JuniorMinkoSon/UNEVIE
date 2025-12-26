package ecom_blog.service;

import ecom_blog.model.Commande;
import ecom_blog.model.Prestataire;
import ecom_blog.model.enums.StatutCommande;
import ecom_blog.model.enums.TypePrestataire;
import ecom_blog.repository.CommandeRepository;
import ecom_blog.repository.PrestataireRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrackingService {

    private final CommandeRepository commandeRepository;
    private final PrestataireRepository prestataireRepository;
    private final EmailService emailService;
    private final SimpMessagingTemplate messagingTemplate;

    public TrackingService(CommandeRepository commandeRepository,
            PrestataireRepository prestataireRepository,
            EmailService emailService,
            SimpMessagingTemplate messagingTemplate) {
        this.commandeRepository = commandeRepository;
        this.prestataireRepository = prestataireRepository;
        this.emailService = emailService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Détermine le type de prestataire requis selon le produit/service
     */
    public TypePrestataire determinerTypePrestataire(Commande commande) {
        if (commande.getProduit() == null) {
            return TypePrestataire.AUTRE;
        }

        String categorie = commande.getProduit().getCategorie();
        if (categorie == null) {
            return TypePrestataire.LIVREUR;
        }

        // Logique de détermination selon la catégorie
        return switch (categorie.toLowerCase()) {
            case "voiture", "véhicule" -> TypePrestataire.CHAUFFEUR;
            case "poulet", "nourriture", "alimentation" -> TypePrestataire.LIVREUR;
            case "résidence", "appartement", "service" -> TypePrestataire.TECHNICIEN;
            default -> TypePrestataire.LIVREUR;
        };
    }

    /**
     * Assigne un prestataire disponible à une commande
     */
    @Transactional
    public Commande assignerPrestataire(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        // Déterminer le type de prestataire requis
        TypePrestataire typeRequis = determinerTypePrestataire(commande);

        // Rechercher un prestataire disponible du bon type
        List<Prestataire> prestatairesDisponibles = prestataireRepository
                .findByDisponibleTrueAndTypePrestataire(typeRequis);

        if (prestatairesDisponibles.isEmpty()) {
            throw new RuntimeException("Aucun prestataire disponible de type " + typeRequis);
        }

        // Assigner le premier prestataire disponible
        Prestataire prestataire = prestatairesDisponibles.get(0);
        prestataire.setDisponible(false);
        prestataire.setEnService(true);
        prestataireRepository.save(prestataire);

        // Mettre à jour la commande
        commande.setPrestataire(prestataire);
        commande.setStatutDetaille(StatutCommande.PRESTATAIRE_ASSIGNE);
        commande.setStatut("PRESTATAIRE_ASSIGNE");
        commande.setDatePriseEnCharge(LocalDateTime.now());
        commandeRepository.save(commande);

        // Envoyer notification email
        emailService.envoyerEmailPrestataireAssigne(commande);

        // Diffuser via WebSocket
        diffuserMiseAJour(commandeId);

        return commande;
    }

    /**
     * Met à jour la position GPS d'un prestataire
     */
    @Transactional
    public void updatePositionPrestataire(Long prestataireId, Double latitude, Double longitude) {
        Prestataire prestataire = prestataireRepository.findById(prestataireId)
                .orElseThrow(() -> new RuntimeException("Prestataire introuvable"));

        prestataire.setLatitude(latitude);
        prestataire.setLongitude(longitude);
        prestataireRepository.save(prestataire);

        // Trouver la commande associée et diffuser la mise à jour
        // (On suppose qu'un prestataire en service n'a qu'une seule commande active)
        diffuserPositionPrestataire(prestataireId, latitude, longitude);
    }

    /**
     * Change le statut d'une commande
     */
    @Transactional
    public Commande changerStatut(Long commandeId, StatutCommande nouveauStatut) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        commande.setStatutDetaille(nouveauStatut);
        commande.setStatut(nouveauStatut.name());

        // Actions spécifiques selon le statut
        switch (nouveauStatut) {
            case CREEE:
            case RECHERCHE_PRESTATAIRE:
            case PRESTATAIRE_ASSIGNE:
                // Pas d'action spécifique pour ces statuts
                break;
            case EN_ROUTE:
                emailService.envoyerEmailPrestataireEnRoute(commande);
                break;
            case ARRIVEE_IMMINENTE:
                // Notification d'arrivée imminente (optionnel)
                break;
            case LIVREE:
                commande.setDateLivraison(LocalDateTime.now());
                // Libérer le prestataire
                if (commande.getPrestataire() != null) {
                    Prestataire prestataire = commande.getPrestataire();
                    prestataire.setDisponible(true);
                    prestataire.setEnService(false);
                    prestataireRepository.save(prestataire);
                }
                emailService.envoyerEmailCommandeLivree(commande);
                break;
            case ANNULEE:
                // Libérer le prestataire si assigné
                if (commande.getPrestataire() != null) {
                    Prestataire prestataire = commande.getPrestataire();
                    prestataire.setDisponible(true);
                    prestataire.setEnService(false);
                    prestataireRepository.save(prestataire);
                }
                break;
        }

        commandeRepository.save(commande);
        diffuserMiseAJour(commandeId);

        return commande;
    }

    /**
     * Récupère les informations de suivi d'une commande
     */
    public Map<String, Object> getTrackingInfo(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        Map<String, Object> info = new HashMap<>();
        info.put("commande", commande);
        info.put("statut", commande.getStatutDetaille());

        if (commande.getPrestataire() != null) {
            info.put("prestataire", commande.getPrestataire());
            info.put("prestatairePosition", Map.of(
                    "latitude", commande.getPrestataire().getLatitude(),
                    "longitude", commande.getPrestataire().getLongitude()));
        }

        if (commande.getLatitudeDestination() != null) {
            info.put("destination", Map.of(
                    "latitude", commande.getLatitudeDestination(),
                    "longitude", commande.getLongitudeDestination()));
        }

        return info;
    }

    /**
     * Diffuse une mise à jour de position via WebSocket
     */
    private void diffuserPositionPrestataire(Long prestataireId, Double latitude, Double longitude) {
        Map<String, Object> update = new HashMap<>();
        update.put("type", "POSITION_UPDATE");
        update.put("prestataireId", prestataireId);
        update.put("latitude", latitude);
        update.put("longitude", longitude);
        update.put("timestamp", LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/tracking", update);
    }

    /**
     * Diffuse une mise à jour de commande via WebSocket
     */
    private void diffuserMiseAJour(Long commandeId) {
        Map<String, Object> update = new HashMap<>();
        update.put("type", "STATUS_UPDATE");
        update.put("commandeId", commandeId);
        update.put("timestamp", LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/tracking/" + commandeId, update);
    }

    /**
     * Simulation du déplacement d'un prestataire (pour tests)
     */
    public void simulerDeplacementPrestataire(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        if (commande.getPrestataire() == null ||
                commande.getLatitudeDestination() == null) {
            throw new RuntimeException("Prestataire ou destination non défini");
        }

        Prestataire prestataire = commande.getPrestataire();
        Double latDepart = prestataire.getLatitude();
        Double lngDepart = prestataire.getLongitude();
        Double latArrivee = commande.getLatitudeDestination();
        Double lngArrivee = commande.getLongitudeDestination();

        // Simulation simple (à améliorer avec un vrai calcul de trajet)
        // Cette méthode devrait être appelée périodiquement par un scheduler
        Double deltaLat = (latArrivee - latDepart) / 10;
        Double deltaLng = (lngArrivee - lngDepart) / 10;

        updatePositionPrestataire(
                prestataire.getId(),
                latDepart + deltaLat,
                lngDepart + deltaLng);
    }
}
