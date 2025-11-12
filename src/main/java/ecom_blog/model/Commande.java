package ecom_blog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "commande")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateCommande = LocalDateTime.now();
    private Double total;
    private String statut = "EN_ATTENTE";

    // ✅ Nouveau champ ajouté
    private String modePaiement; // ex: "CARTE", "MOBILE_MONEY", "CASH"

    // ✅ Lien avec l’utilisateur
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // ✅ Lien avec le produit commandé
    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    // === GETTERS & SETTERS ===

    public Long getId() {
        return id;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }
}
