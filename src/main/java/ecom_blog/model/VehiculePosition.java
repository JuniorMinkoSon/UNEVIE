package ecom_blog.model;

import ecom_blog.model.enums.StatutVehicule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Stocke la position GPS en temps réel d'un véhicule
 */
@Entity
@Table(name = "vehicule_positions")
@Getter
@Setter
public class VehiculePosition extends BaseEntity {

    @Column(nullable = false)
    private Long vehiculeId; // ID du Produit (véhicule)

    private Long commandeId; // Commande en cours (si applicable)

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private LocalDateTime timestamp; // Moment de la position

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutVehicule statut = StatutVehicule.DISPONIBLE;

    private Double vitesse; // Vitesse actuelle en km/h

    private Double direction; // Direction en degrés (0-360)

    private Double altitude; // Altitude en mètres

    private Double precision; // Précision GPS en mètres

    @ManyToOne
    @JoinColumn(name = "noeud_proche_id")
    private RoadNode noeudProche; // Nœud routier le plus proche

    private String adresseApproximative; // Adresse textuelle approximative

    @PrePersist
    public void prePersist() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
    }
}
