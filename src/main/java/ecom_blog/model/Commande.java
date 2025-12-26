package ecom_blog.model;

import ecom_blog.model.enums.StatutCommande;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "commandes")
@Getter
@Setter
public class Commande extends BaseEntity {

    @Column(nullable = false)
    private LocalDateTime dateCommande = LocalDateTime.now();

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private String statut = "EN_ATTENTE";

    @Column(nullable = false)
    private String modePaiement;

    // 🧑 Infos client (commande sans compte possible)
    @Column(nullable = false)
    private String nomClient;

    @Column(nullable = false)
    private String telephone;

    @Column(nullable = false)
    private String adresse;

    // 🔗 Utilisateur connecté (optionnel)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 🚗 Champs spécifiques Voiture
    private Boolean avecChauffeur;
    private String lieuPriseEnCharge; // Localisation spécifique voiture

    // 🏠 Champs spécifiques Résidence
    private Integer nombreJours;

    // 🍗 Champs spécifiques Poulet
    private Integer quantite;

    // 📍 Localisation générale (utilisé si lieuPrise EnCharge non spé)
    private String localisation;

    // 📱 Opérateur Mobile Money
    private String operateur;

    // 🔗 Produit commandé
    @ManyToOne(optional = true)
    @JoinColumn(name = "produit_id", nullable = true)
    private Produit produit;

    // 🚀 NOUVEAUX CHAMPS POUR LE SUIVI EN TEMPS RÉEL

    // Statut détaillé de la commande
    @Enumerated(EnumType.STRING)
    private StatutCommande statutDetaille;

    // Prestataire assigné
    @ManyToOne
    @JoinColumn(name = "prestataire_id")
    private Prestataire prestataire;

    // Coordonnées GPS de la destination
    private Double latitudeDestination;
    private Double longitudeDestination;

    // Horodatage
    private LocalDateTime datePriseEnCharge; // Date d'assignation du prestataire
    private LocalDateTime dateLivraison; // Date de livraison effective
}
