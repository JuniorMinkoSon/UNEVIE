package ecom_blog.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titre;
    private String categorie;

    @Column(length = 5000)
    private String contenu;

    private String imageUrl;
    private LocalDateTime datePublication = LocalDateTime.now();

    // --- Getters et Setters ---
    public Long getId() { return id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getDatePublication() { return datePublication; }
    public void setDatePublication(LocalDateTime datePublication) { this.datePublication = datePublication; }
}
