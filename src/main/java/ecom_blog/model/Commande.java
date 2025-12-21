package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "commande")
@Getter @Setter
public class Commande extends BaseEntity{


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
    @Id
    private Long id;

}
