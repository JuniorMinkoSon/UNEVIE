package ecom_blog.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;  // Client

    private LocalDate dateCommande = LocalDate.now();

    private String statut = "EN_ATTENTE"; // EN_ATTENTE, EN_COURS, LIVRÉ

    private double total;
}
