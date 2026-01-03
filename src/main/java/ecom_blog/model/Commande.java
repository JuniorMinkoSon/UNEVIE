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
}
