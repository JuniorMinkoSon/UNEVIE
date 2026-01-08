package ecom_blog.service;

import ecom_blog.dto.TrackingDto;
import ecom_blog.model.Commande;
import ecom_blog.model.PositionLivreur;
import ecom_blog.model.User;
import ecom_blog.repository.PositionLivreurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TrackingService - Gère le suivi GPS en temps réel des livreurs
 */
@Service
public class TrackingService {

    private static final Logger logger = LoggerFactory.getLogger(TrackingService.class);

    @Autowired
    private PositionLivreurRepository positionRepository;

    @Autowired
    private MapboxService mapboxService;

    @Autowired(required = false)
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Enregistre une nouvelle position et diffuse via WebSocket
     */
    @Transactional
    public void enregistrerPosition(User livreur, Commande commande,
            double latitude, double longitude,
            Double vitesse, Double cap) {

        logger.debug("Enregistrement position livreur {} pour commande {}: {}, {}",
                livreur.getId(), commande.getId(), latitude, longitude);

        // Créer et sauvegarder la position
        PositionLivreur position = new PositionLivreur();
        position.setLivreur(livreur);
        position.setCommande(commande);
        position.setLatitude(latitude);
        position.setLongitude(longitude);
        position.setTimestamp(LocalDateTime.now());
        position.setVitesse(vitesse);
        position.setCap(cap);

        positionRepository.save(position);

        // Mettre à jour la position du livreur dans User
        livreur.setLatitude(latitude);
        livreur.setLongitude(longitude);

        // Calculer ETA et distance restante
        long eta = 0;
        double distanceRestante = 0;

        if (commande.getLatitudeDestination() != null &&
                commande.getLongitudeDestination() != null) {

            eta = mapboxService.calculerETA(
                    longitude, latitude,
                    commande.getLongitudeDestination(),
                    commande.getLatitudeDestination());

            distanceRestante = mapboxService.calculerDistance(
                    longitude, latitude,
                    commande.getLongitudeDestination(),
                    commande.getLatitudeDestination());
        }

        // Créer DTO pour WebSocket
        TrackingDto dto = new TrackingDto();
        dto.setLatitude(latitude);
        dto.setLongitude(longitude);
        dto.setEta(eta);
        dto.setTimestamp(LocalDateTime.now());
        dto.setVitesse(vitesse);
        dto.setCap(cap);
        dto.setDistanceRestante(distanceRestante);

        // Informations livreur
        dto.setLivreurNom(livreur.getNom());
        dto.setLivreurTelephone(livreur.getTelephone());

        // Diffuser via WebSocket
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend(
                    "/topic/livraison/" + commande.getId(), dto);

            logger.debug("Position diffusée via WebSocket pour commande {}", commande.getId());
        }
    }

    /**
     * Récupère l'historique des positions d'une commande
     */
    public List<PositionLivreur> getHistorique(Commande commande) {
        return positionRepository.findByCommandeOrderByTimestampDesc(commande);
    }

    /**
     * Récupère la dernière position d'un livreur
     */
    public PositionLivreur getDernierePosition(User livreur) {
        return positionRepository.findFirstByLivreurOrderByTimestampDesc(livreur)
                .orElse(null);
    }

    /**
     * Récupère le tracking actuel d'une commande
     */
    public TrackingDto getTrackingActuel(Commande commande) {
        List<PositionLivreur> positions = getHistorique(commande);

        if (positions.isEmpty()) {
            // Fallback 1: Utiliser la dernière position connue du livreur (si dispo)
            if (commande.getLivreur() != null && commande.getLivreur().getLatitude() != null) {
                TrackingDto dto = new TrackingDto();
                dto.setLatitude(commande.getLivreur().getLatitude());
                dto.setLongitude(commande.getLivreur().getLongitude());
                dto.setTimestamp(LocalDateTime.now());
                dto.setVitesse(0.0);
                dto.setCap(0.0);
                dto.setLivreurNom(commande.getLivreur().getNom());
                dto.setLivreurTelephone(commande.getLivreur().getTelephone());

                // Calculer ETA
                if (commande.getLatitudeDestination() != null) {
                    long eta = mapboxService.calculerETA(
                            dto.getLongitude(), dto.getLatitude(),
                            commande.getLongitudeDestination(), commande.getLatitudeDestination());
                    dto.setEta(eta);
                }
                return dto;
            }

            // Fallback 2 (DEMO): Retourner coordonnées par défaut (Dakar) si aucune info
            TrackingDto dto = new TrackingDto();
            dto.setLatitude(14.6937);
            dto.setLongitude(-17.4677);
            dto.setTimestamp(LocalDateTime.now());
            dto.setVitesse(0.0);
            dto.setCap(0.0);
            if (commande.getLivreur() != null) {
                dto.setLivreurNom(commande.getLivreur().getNom());
                dto.setLivreurTelephone(commande.getLivreur().getTelephone());
            } else {
                dto.setLivreurNom("Livreur");
                dto.setLivreurTelephone("---");
            }

            // Calculer ETA Default
            if (commande.getLatitudeDestination() != null) {
                long eta = mapboxService.calculerETA(
                        dto.getLongitude(), dto.getLatitude(),
                        commande.getLongitudeDestination(), commande.getLatitudeDestination());
                dto.setEta(eta);
            }
            return dto;
        }

        PositionLivreur derniere = positions.get(0);
        TrackingDto dto = new TrackingDto();
        dto.setLatitude(derniere.getLatitude());
        dto.setLongitude(derniere.getLongitude());
        dto.setTimestamp(derniere.getTimestamp());
        dto.setVitesse(derniere.getVitesse());
        dto.setCap(derniere.getCap());

        // Calculer ETA
        if (commande.getLatitudeDestination() != null &&
                commande.getLongitudeDestination() != null) {

            long eta = mapboxService.calculerETA(
                    derniere.getLongitude(), derniere.getLatitude(),
                    commande.getLongitudeDestination(),
                    commande.getLatitudeDestination());
            dto.setEta(eta);
        }

        // Informations livreur
        if (commande.getLivreur() != null) {
            dto.setLivreurNom(commande.getLivreur().getNom());
            dto.setLivreurTelephone(commande.getLivreur().getTelephone());
        }

        return dto;
    }
}
