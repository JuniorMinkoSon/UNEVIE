package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "produit")
@Getter @Setter
public class Produit extends BaseEntity{

    private String nom;
    private String categorie;
    private Double prix;
    private String description;
    private String imageUrl;
    private boolean disponible = true;

    public boolean isDisponible() { return disponible; }

}
