package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Commande extends BaseEntity {

    /* ================== RELATIONS ================== */

    @ManyToOne
    private User user;

    @ManyToOne
    private User livreur;

    @ManyToOne
    private Produit produit;

    @ManyToOne
    private ServiceFournisseur service;

    /* ================== DONNÉES COMMANDE ================== */

    private int quantite;
    private int nombreJours;
    private Double total;

    private String statut;
    private String modePaiement;
    private String operateur;

    private String adresse;
    private String localisation;

    private LocalDateTime dateCommande;

    /* ================== CHAMPS PREMIUM / FORMULAIRE ================== */

    private String nomClient;
    private String telephone;
    private boolean avecChauffeur;
    private String lieuPriseEnCharge;

    /* ================== LIVRAISON ================== */

    private double distanceEstimee;
    private long dureeEstimee;
    private long debutLivraison;

    /* ================== CRÉNEAUX HORAIRES ================== */

    private LocalDateTime dateCreneauDebut;
    private LocalDateTime dateCreneauFin;

    @ManyToOne
    @JoinColumn(name = "creneau_id")
    private CreneauHoraire creneau;

    /* ================== TIMER & EXPIRATION ================== */

    private LocalDateTime dateCreation;
    private LocalDateTime dateExpiration; // dateCreation + 10 minutes

    @Column(nullable = false)
    private boolean expiree = false;

    @Column(nullable = false)
    private boolean redirigee = false;

    /* ================== MASQUAGE DONNÉES ================== */

    @Column(nullable = false)
    private boolean donneesVisiblesFournisseur = false; // true après acceptation

    /* ================== POLITIQUE/CONTRAT ================== */

    @ManyToOne
    @JoinColumn(name = "politique_id")
    private Politique politique;

    @Column(nullable = false)
    private boolean politiqueAcceptee = false;

    private LocalDateTime datePolitiqueAcceptee;

    /* ================== ÉVALUATION ================== */

    @Column(nullable = false)
    private boolean evaluationEnvoyee = false;

    private LocalDateTime dateEvaluationEnvoyee;
    private LocalDateTime dateExecution; // Date de fin de livraison

    /* ================== TRACKING GPS ================== */

    private Double latitudeDestination;
    private Double longitudeDestination;
}
