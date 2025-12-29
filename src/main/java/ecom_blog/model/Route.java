package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente une route calculée entre deux points
 */
@Entity
@Table(name = "routes")
@Getter
@Setter
public class Route extends BaseEntity {

    private Long commandeId; // Commande associée

    private Long vehiculeId; // Véhicule assigné

    @ManyToOne
    @JoinColumn(name = "depart_node_id")
    private RoadNode pointDepart; // Nœud de départ

    @ManyToOne
    @JoinColumn(name = "arrivee_node_id")
    private RoadNode pointArrivee; // Nœud d'arrivée

    @Column(columnDefinition = "TEXT")
    private String cheminOptimal; // JSON: liste des nœuds du chemin

    @Column(columnDefinition = "TEXT")
    private String coordonneesGPS; // JSON: coordonnées GPS détaillées

    @Column(nullable = false)
    private Double distanceTotaleKm; // Distance totale en km

    @Column(nullable = false)
    private Integer dureeEstimeeMin; // Durée estimée en minutes

    private LocalDateTime heureDepart; // Heure prévue de départ

    private LocalDateTime heureArriveeEstimee; // Heure d'arrivée estimée

    private LocalDateTime heureArriveeReelle; // Heure d'arrivée réelle

    private Boolean routeActive = false; // Si le trajet est en cours

    private Integer progressionPourcent = 0; // Progression 0-100%

    @ManyToMany
    @JoinTable(name = "route_nodes", joinColumns = @JoinColumn(name = "route_id"), inverseJoinColumns = @JoinColumn(name = "node_id"))
    private List<RoadNode> noeudsTrajet = new ArrayList<>();

    /**
     * Calcule l'heure d'arrivée estimée
     */
    public void calculateETA() {
        if (this.heureDepart != null && this.dureeEstimeeMin != null) {
            this.heureArriveeEstimee = this.heureDepart.plusMinutes(this.dureeEstimeeMin);
        }
    }

    @PrePersist
    @PreUpdate
    public void updateETA() {
        calculateETA();
    }
}
