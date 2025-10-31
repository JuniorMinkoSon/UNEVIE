package ecom_blog.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    @Column(length = 2000)
    private String contenu;
    private String categorie;
    private String imageUrl;
    private LocalDate datePublication = LocalDate.now();
}
