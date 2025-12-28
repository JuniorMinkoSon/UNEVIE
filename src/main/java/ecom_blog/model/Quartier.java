package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "quartiers")
@Getter
@Setter
public class Quartier extends BaseEntity {

    @Column(nullable = false)
    private String nom; // Ex: Cocody, Yopougon, Plateau

    @Column(nullable = false)
    private String commune; // Ex: Abidjan, Grand-Bassam

    // Coordonnées GPS du centre du quartier
    private Double latitude;
    private Double longitude;

    // Rayon de couverture en km
    private Double rayonCouverture;
}
