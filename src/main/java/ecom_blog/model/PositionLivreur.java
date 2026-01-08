package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * PositionLivreur - Historique des positions GPS des livreurs
 * Utilisé pour le tracking en temps réel et l'historique des trajets
 */
@Entity
@Table(name = "position_livreur")
@Getter
@Setter
public class PositionLivreur extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id", nullable = false)
    private User livreur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // ================== DONNÉES MAPBOX ==================

    private Double vitesse; // km/h

    private Double cap; // Direction en degrés (0-360)

    private Double altitude; // Altitude en mètres (optionnel)

    private Double precision; // Précision GPS en mètres
}
