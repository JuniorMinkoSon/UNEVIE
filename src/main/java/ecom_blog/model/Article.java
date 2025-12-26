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
    @Column(columnDefinition = "TEXT")
    private String contenu;

    private String imageUrl;
    @Column(unique = true)
    private String slug;

    @JsonBackReference
    @ManyToOne
    private Categorie category;

}
