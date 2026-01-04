package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Reservation - Entité centrale pour les réservations de services
 * La commission de 5% est calculée lors de l'acceptation par le fournisseur
 */
@Entity
@Table(name = "reservation")
@Getter
@Setter
public class Reservation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceFournisseur service;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Fournisseur fournisseur;

    private LocalDateTime dateReservation;

    // Date prévue du service
    private LocalDate dateService;
    private LocalDate dateFinService; // Pour les locations multi-jours

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutReservation statut = StatutReservation.EN_COURS;

    // Montants
    @Column(nullable = false)
    private Double montant;

    // Commission UneVie (5% du montant)
    private Double commission = 0.0;

    // Montant net pour le fournisseur (montant - commission)
    private Double montantNet;

    // Informations client
    private String nomClient;
    private String telephoneClient;
    private String emailClient;

    // Localisation (pour les services sur place)
    private String adresseLivraison;

    // Notes et commentaires
    @Column(length = 1000)
    private String notesClient;

    @Column(length = 1000)
    private String notesFournisseur;

    // Mode de paiement
    private String modePaiement;
    private String operateur; // Mobile Money: Wave, Orange, MTN, Moov

    // Options spécifiques selon le secteur
    private Integer nombrePersonnes; // Événementiel, Alimentaire
    private Integer nombreJours; // Voitures
    private boolean avecChauffeur; // Voitures

    // Contrat juridique (pour le secteur VOITURE)
    @Column(columnDefinition = "TEXT")
    private String contratContenu;
    private boolean contratSigne = false;

    /**
     * Calcule et applique la commission de 5% lors de l'acceptation
     */
    public void calculerCommission() {
        if (this.montant != null) {
            this.commission = this.montant * 0.05; // 5% de commission
            this.montantNet = this.montant - this.commission;
        }
    }
}
