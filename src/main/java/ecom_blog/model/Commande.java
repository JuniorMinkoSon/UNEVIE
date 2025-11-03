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

    // Explicit setters/getters to ensure availability
    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getStatut() {
        return this.statut;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotal() {
        return this.total;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
