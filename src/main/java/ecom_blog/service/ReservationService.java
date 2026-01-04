package ecom_blog.service;

import ecom_blog.model.*;
import ecom_blog.repository.FournisseurRepository;
import ecom_blog.repository.ReservationRepository;
import ecom_blog.repository.ServiceFournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReservationService {

    private static final double TAUX_COMMISSION = 0.05; // 5% de commission

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private ServiceFournisseurRepository serviceFournisseurRepository;

    // ==================== CRUD ====================

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    // ==================== CRÉATION DE RÉSERVATION ====================

    /**
     * Crée une nouvelle réservation avec statut EN_COURS
     */
    public Reservation creerReservation(User client, Long serviceId,
            String nomClient, String telephone, String email,
            java.time.LocalDate dateService, java.time.LocalDate dateFinService,
            Integer nombrePersonnes, Integer nombreJours,
            boolean avecChauffeur, String notes) {

        if (serviceId == null) {
            throw new IllegalArgumentException("L'ID du service ne peut pas être nul");
        }

        ServiceFournisseur service = serviceFournisseurRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service non trouvé"));

        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setService(service);
        reservation.setFournisseur(service.getFournisseur());
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut(StatutReservation.EN_COURS);

        // Infos client
        reservation.setNomClient(nomClient);
        reservation.setTelephoneClient(telephone);
        reservation.setEmailClient(email);

        // Dates
        reservation.setDateService(dateService);
        reservation.setDateFinService(dateFinService);

        // Options selon secteur
        reservation.setNombrePersonnes(nombrePersonnes);
        reservation.setNombreJours(nombreJours);
        reservation.setAvecChauffeur(avecChauffeur);
        reservation.setNotesClient(notes);

        // Calcul du montant
        double montant = calculerMontant(service, nombreJours);
        reservation.setMontant(montant);

        // Générer le contrat si c'est une voiture
        if (service.getSecteur() == Secteur.VOITURE) {
            reservation.setContratContenu(genererContratLocation(reservation));
            reservation.setContratSigne(true); // Signé par acceptation du formulaire
        }

        return reservationRepository.save(reservation);
    }

    /**
     * Génère le texte d'un contrat de location de voiture
     */
    private String genererContratLocation(Reservation r) {
        StringBuilder sb = new StringBuilder();
        sb.append("CONTRAT DE LOCATION DE VÉHICULE\n\n");
        sb.append("Entre d'une part :\n");
        sb.append("L'entreprise ").append(r.getFournisseur().getNomEntreprise()).append("\n");
        sb.append("Adresse : ").append(r.getFournisseur().getAdresse()).append(", ")
                .append(r.getFournisseur().getVille()).append("\n\n");

        sb.append("Et d'autre part :\n");
        sb.append("M/Mme ").append(r.getNomClient()).append("\n");
        sb.append("Téléphone : ").append(r.getTelephoneClient()).append("\n\n");

        sb.append("II A ÉTÉ CONVENU CE QUI SUIT :\n\n");
        sb.append("1. OBJET DU CONTRAT : Le loueur met à disposition du client le véhicule : ")
                .append(r.getService().getNom()).append(".\n");
        sb.append("2. DURÉE : La location est prévue du ").append(r.getDateService()).append(" au ")
                .append(r.getDateFinService()).append(".\n");
        sb.append("3. MONTANT : Le montant total de la location est de ").append(r.getMontant()).append(" FCFA.\n");
        if (r.isAvecChauffeur()) {
            sb.append("4. OPTION : La location inclut un chauffeur.\n");
        }
        sb.append(
                "\nLe client s'engage à restituer le véhicule dans l'état où il l'a reçu et à respecter les conditions d'utilisation définies par le loueur.");

        return sb.toString();
    }

    /**
     * Calcule le montant selon le type de service
     */
    private double calculerMontant(ServiceFournisseur service, Integer nombreJours) {
        if (nombreJours != null && nombreJours > 0 && service.getPrixParJour() != null) {
            return service.getPrixParJour() * nombreJours;
        }
        return service.getPrix();
    }

    // ==================== GESTION DES STATUTS ====================

    /**
     * Accepte une réservation et applique la commission de 5%
     */
    public Reservation accepterReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        if (reservation.getStatut() != StatutReservation.EN_COURS) {
            throw new RuntimeException("Cette réservation ne peut plus être acceptée");
        }

        reservation.setStatut(StatutReservation.ACCEPTE);

        // Calcul de la commission 5%
        reservation.calculerCommission();

        // Mise à jour des statistiques fournisseur
        Fournisseur fournisseur = reservation.getFournisseur();
        fournisseur.setNombreReservations(fournisseur.getNombreReservations() + 1);
        fournisseur.setChiffreAffaires(fournisseur.getChiffreAffaires() + reservation.getMontant());
        fournisseur.setTotalCommissions(fournisseur.getTotalCommissions() + reservation.getCommission());
        fournisseurRepository.save(fournisseur);

        // Mise à jour statistiques service
        ServiceFournisseur service = reservation.getService();
        service.setNombreReservations(service.getNombreReservations() + 1);
        serviceFournisseurRepository.save(service);

        return reservationRepository.save(reservation);
    }

    /**
     * Refuse une réservation
     */
    public Reservation refuserReservation(Long reservationId, String motif) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        if (reservation.getStatut() != StatutReservation.EN_COURS) {
            throw new RuntimeException("Cette réservation ne peut plus être modifiée");
        }

        reservation.setStatut(StatutReservation.REFUSE);
        reservation.setNotesFournisseur(motif);

        return reservationRepository.save(reservation);
    }

    /**
     * Marque une réservation comme terminée
     */
    public Reservation terminerReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        if (reservation.getStatut() != StatutReservation.ACCEPTE) {
            throw new RuntimeException("Seule une réservation acceptée peut être terminée");
        }

        reservation.setStatut(StatutReservation.TERMINE);
        return reservationRepository.save(reservation);
    }

    // ==================== LISTES ====================

    public List<Reservation> getReservationsClient(Long clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    public List<Reservation> getReservationsFournisseur(Long fournisseurId) {
        return reservationRepository.findByFournisseurId(fournisseurId);
    }

    public List<Reservation> getReservationsEnCours(Fournisseur fournisseur) {
        return reservationRepository.findByFournisseurAndStatut(fournisseur, StatutReservation.EN_COURS);
    }

    public List<Reservation> getReservationsAcceptees(Fournisseur fournisseur) {
        return reservationRepository.findByFournisseurAndStatut(fournisseur, StatutReservation.ACCEPTE);
    }

    // ==================== STATISTIQUES ADMIN ====================

    /**
     * Total des commissions perçues par UneVie
     */
    public Double getTotalCommissions() {
        Double total = reservationRepository.sumTotalCommission();
        return total != null ? total : 0.0;
    }

    /**
     * Total des revenus (toutes réservations acceptées)
     */
    public Double getTotalRevenus() {
        Double total = reservationRepository.sumTotalMontant();
        return total != null ? total : 0.0;
    }

    /**
     * Statistiques mensuelles pour une année donnée
     * Retourne: mois, nombre de réservations, montant total, commissions
     */
    public List<Object[]> getStatistiquesMensuelles(int year) {
        return reservationRepository.getStatistiquesMensuelles(year);
    }

    /**
     * Statistiques par fournisseur
     * Retourne: id, nom, nombre de réservations, montant total, commissions
     */
    public List<Object[]> getStatistiquesParFournisseur() {
        return reservationRepository.getStatistiquesParFournisseur();
    }

    /**
     * Commissions par fournisseur spécifique
     */
    public Double getCommissionsByFournisseur(Long fournisseurId) {
        Double total = reservationRepository.getTotalCommissionsByFournisseur(fournisseurId);
        return total != null ? total : 0.0;
    }

    /**
     * Revenus nets pour un fournisseur
     */
    public Double getRevenusNetByFournisseur(Long fournisseurId) {
        Double total = reservationRepository.getTotalRevenusNetByFournisseur(fournisseurId);
        return total != null ? total : 0.0;
    }

    /**
     * Nombre de réservations par statut pour un fournisseur
     */
    public long countByFournisseurAndStatut(Long fournisseurId, StatutReservation statut) {
        return reservationRepository.countByFournisseurIdAndStatut(fournisseurId, statut);
    }
}
