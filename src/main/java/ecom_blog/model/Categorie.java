package ecom_blog.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "categorie")
@Getter
@Setter
public class Categorie extends BaseEntity {
    private String libelle;
    private String description;

    @JsonManagedReference
    @OneToMany(mappedBy = "category")
    private List<Article> articles;

}
