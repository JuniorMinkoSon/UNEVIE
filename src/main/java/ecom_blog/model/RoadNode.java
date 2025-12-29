package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un nœud (point) dans le réseau routier
 * Utilisé pour l'algorithme de Dijkstra
 */
@Entity
@Table(name = "road_nodes")
@Getter
@Setter
public class RoadNode extends BaseEntity {

    @Column(nullable = false)
    private String nom; // Ex: "Cocody Riviera"

    private String description; // Description du point

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    private String typeNode; // intersection, quartier, landmark

    // Relations sortantes (arêtes partant de ce nœud)
    @OneToMany(mappedBy = "nodeDepart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoadEdge> connexionsSortantes = new ArrayList<>();

    // Relations entrantes (arêtes arrivant à ce nœud)
    @OneToMany(mappedBy = "nodeArrivee")
    private List<RoadEdge> connexionsEntrantes = new ArrayList<>();

    /**
     * Calcule la distance euclidienne entre ce nœud et un autre
     * Formule Haversine pour coordonnées GPS
     */
    public double distanceTo(RoadNode other) {
        final int R = 6371; // Rayon de la Terre en km

        double latDistance = Math.toRadians(other.latitude - this.latitude);
        double lonDistance = Math.toRadians(other.longitude - this.longitude);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude))
                        * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distance en km
    }
}
