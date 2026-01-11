package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class MessageContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    private String email;

    private String telephone;

    private String sujet;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime dateEnvoi = LocalDateTime.now();
}
