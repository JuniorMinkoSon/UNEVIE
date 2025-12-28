package ecom_blog.model;

import ecom_blog.model.enums.StatutCreneau;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "creneaux_disponibilite")
@Getter
@Setter
public class CreneauDisponibilite extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Column(nullable = false)
    private LocalDateTime dateDebut;

    @Column(nullable = false)
    private LocalDateTime dateFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCreneau statut = StatutCreneau.DISPONIBLE;

    // Si réservé, lien vers la commande
    @OneToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @Column(length = 500)
    private String notes; // Notes internes (ex: "Maintenance prévue", etc.)

    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
}
