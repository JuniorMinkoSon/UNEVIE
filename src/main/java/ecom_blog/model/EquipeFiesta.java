package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "equipes_fiesta")
@Getter
@Setter
public class EquipeFiesta extends BaseEntity {

    @Column(nullable = false)
    private String nomEquipe; // Nom personnalisé par l'utilisateur

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User createur;

    @Column(nullable = false)
    private LocalDate dateEvenement;

    // Entreprises sélectionnées pour cette équipe
    @ManyToMany
    @JoinTable(name = "equipe_fiesta_entreprises", joinColumns = @JoinColumn(name = "equipe_id"), inverseJoinColumns = @JoinColumn(name = "entreprise_id"))
    private List<Entreprise> entreprises;

    private Double budgetTotal;

    @Column(length = 2000)
    private String notes; // Notes et personnalisations

    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    // Membres de l'équipe (optionnel pour partage)
    @ManyToMany
    @JoinTable(name = "equipe_fiesta_membres", joinColumns = @JoinColumn(name = "equipe_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> membres;
}
