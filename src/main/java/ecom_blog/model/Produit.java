package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "produit")
@Getter
@Setter
public class Produit extends BaseEntity {

    private String nom;
    private String categorie;
    private Double prix;

    @Column(length = 5000)
    private String description;

    private String imageUrl; // Image principale

    @ElementCollection
    @CollectionTable(name = "produit_images", joinColumns = @JoinColumn(name = "produit_id"))
    @Column(name = "image_url")
    private java.util.List<String> imageUrls = new java.util.ArrayList<>();

    private boolean disponible = true;

    public boolean isDisponible() {
        return disponible;
    }
}
