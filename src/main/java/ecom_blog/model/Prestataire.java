package ecom_blog.model;

import ecom_blog.model.enums.TypePrestataire;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "prestataires")
@Getter
@Setter
public class Prestataire extends BaseEntity {

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypePrestataire typePrestataire;

    // Véhicule (optionnel - pour CHAUFFEUR et LIVREUR)
    private String vehicule;

    // Immatriculation (optionnel)
    private String immatriculation;

    // Position actuelle
    @Column(nullable = true)
    private Double latitude;

    @Column(nullable = true)
    private Double longitude;

    // Statut de disponibilité
    @Column(nullable = false)
    private Boolean disponible = true;

    // En train d'effectuer une livraison
    @Column(nullable = false)
    private Boolean enService = false;

    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    // Lien avec le compte utilisateur pour l'authentification
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
