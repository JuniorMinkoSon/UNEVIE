package ecom_blog.model;

import ecom_blog.model.enums.CategorieEntreprise;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "entreprises")
@Getter
@Setter
public class Entreprise extends BaseEntity {

    @Column(nullable = false)
    private String nom; // Ex: "Babi Location", "Sponguy Location"

    @Column(length = 1000)
    private String description;

    private String logo; // URL ou chemin du logo

    @Column(nullable = false)
    private String telephone;

    private String email;

    private String adresse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategorieEntreprise categorie;

    // Localisation
    @ManyToOne
    @JoinColumn(name = "quartier_id")
    private Quartier quartier;

    private Double latitude;
    private Double longitude;

    // 🤝 Contrat EasyService (UNEVIE agit comme intermédiaire)
    @Column(nullable = false)
    private Boolean contratActif = false;

    private LocalDate dateDebutContrat;
    private LocalDate dateFinContrat;

    @Column(length = 500)
    private String notesContrat; // Notes internes sur le contrat

    // Pourcentage de commission EasyService (par défaut 10%)
    private Double commissionPourcentage = 10.0;

    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    // Relations
    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
    private List<Produit> produits;

    // Note et évaluations (optionnel pour future extension)
    private Double noteGlobale;
    private Integer nombreAvis;
}
