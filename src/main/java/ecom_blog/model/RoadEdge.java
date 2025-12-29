package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Représente une arête (route) entre deux nœuds
 * Utilisé pour l'algorithme de Dijkstra
 */
@Entity
@Table(name = "road_edges")
@Getter
@Setter
public class RoadEdge extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "node_depart_id")
    private RoadNode nodeDepart; // Nœud de départ

    @ManyToOne(optional = false)
    @JoinColumn(name = "node_arrivee_id")
    private RoadNode nodeArrivee; // Nœud d'arrivée

    @Column(nullable = false)
    private Double distanceKm; // Distance en kilomètres

    private Integer dureeMinutes; // Durée estimée en minutes

    private Double vitesseMoyenne = 50.0; // Vitesse moyenne en km/h (défaut: 50 km/h)

    @Column(nullable = false)
    private Double poids; // Poids pour Dijkstra (distance ou temps)

    private String typeRoute; // autoroute, rue, boulevard

    private Boolean bidirectionnelle = true; // Si la route est bidirectionnelle

    private Double coefficientTrafic = 1.0; // Multiplicateur de trafic (1.0 = normal, >1.0 = embouteillage)

    /**
     * Calcule le poids basé sur la distance
     */
    public void calculatePoidsFromDistance() {
        this.poids = this.distanceKm * this.coefficientTrafic;
    }

    /**
     * Calcule le poids basé sur le temps
     */
    public void calculatePoidsFromTime() {
        if (this.dureeMinutes != null) {
            this.poids = this.dureeMinutes * this.coefficientTrafic;
        } else if (this.distanceKm != null && this.vitesseMoyenne != null) {
            this.dureeMinutes = (int) Math.ceil((this.distanceKm / this.vitesseMoyenne) * 60);
            this.poids = this.dureeMinutes * this.coefficientTrafic;
        }
    }

    /**
     * Met à jour la durée si elle n'est pas définie
     */
    @PrePersist
    @PreUpdate
    public void calculateDuree() {
        if (this.dureeMinutes == null && this.distanceKm != null && this.vitesseMoyenne != null) {
            this.dureeMinutes = (int) Math.ceil((this.distanceKm / this.vitesseMoyenne) * 60);
        }
        if (this.poids == null) {
            calculatePoidsFromDistance();
        }
    }
}
