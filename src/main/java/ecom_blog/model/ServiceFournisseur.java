package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * ServiceFournisseur - Représente un service/prestation proposé par un
 * fournisseur
 */
@Entity
@Table(name = "service_fournisseur")
@Getter
@Setter
public class ServiceFournisseur extends BaseEntity {

    @Column(nullable = false)
    private String nom;

    @Column(length = 3000)
    private String description;

    @Column(nullable = false)
    private Double prix;

    // Prix par jour pour les locations
    private Double prixParJour;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Secteur secteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Fournisseur fournisseur;

    private String imageUrl;

    @ElementCollection
    @CollectionTable(name = "service_fournisseur_images", joinColumns = @JoinColumn(name = "service_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    private boolean disponible = true;

    // Durée du service (en heures, optionnel)
    private Integer duree;

    // Capacité (pour les salles événementielles par exemple)
    private Integer capacite;

    // Nombre de réservations effectuées
    private int nombreReservations = 0;

    // Note moyenne (sur 5)
    private Double noteMoyenne;
    private int nombreAvis = 0;
}
