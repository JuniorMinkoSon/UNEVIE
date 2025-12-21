package ecom_blog.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "article")
@Getter
@Setter
public class Article extends BaseEntity {
    private String titre;
    @Column(length = 5000)
    private String contenu;

    private String imageUrl;
    @JsonBackReference
    @ManyToOne
    private Categorie category;

}
