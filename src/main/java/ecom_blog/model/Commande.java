package ecom_blog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    /* ================== LIVRAISON ================== */

    private double distanceEstimee;
    private long dureeEstimee;
    private long debutLivraison;

    /* ================== GETTERS / SETTERS ================== */

    public Long getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public User getLivreur() { return livreur; }
    public void setLivreur(User livreur) { this.livreur = livreur; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public int getNombreJours() { return nombreJours; }
    public void setNombreJours(int nombreJours) { this.nombreJours = nombreJours; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }

    public String getOperateur() { return operateur; }
    public void setOperateur(String operateur) { this.operateur = operateur; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getLocalisation() { return localisation; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }

    public LocalDateTime getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDateTime dateCommande) { this.dateCommande = dateCommande; }

    public double getDistanceEstimee() { return distanceEstimee; }
    public void setDistanceEstimee(double distanceEstimee) { this.distanceEstimee = distanceEstimee; }

    public long getDureeEstimee() { return dureeEstimee; }
    public void setDureeEstimee(long dureeEstimee) { this.dureeEstimee = dureeEstimee; }

    public long getDebutLivraison() { return debutLivraison; }
    public void setDebutLivraison(long debutLivraison) { this.debutLivraison = debutLivraison; }
}
