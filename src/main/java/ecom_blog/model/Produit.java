package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "produit")
public class Produit extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nom;
    private String categorie;
    private Double prix;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String imageUrl;
}
