package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Fournisseur - Entreprise ou prestataire de services sur la plateforme UneVie
 */
@Entity
@Table(name = "fournisseur")
@Getter
@Setter
public class Fournisseur extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String nomEntreprise;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Secteur secteur;

    @Column(length = 2000)
    private String description;

    private String adresse;
    private String ville;
    private String telephone;

    private String logoUrl;

    @ElementCollection
    @CollectionTable(name = "fournisseur_images", joinColumns = @JoinColumn(name = "fournisseur_id"))
    @Column(name = "image_url")
    private List<String> imagesGalerie = new ArrayList<>();

    // Contrat juridique
    private boolean contratAccepte = false;
    private LocalDateTime dateContrat;

    // Statistiques
    private int nombreReservations = 0;
    private double chiffreAffaires = 0.0;
    private double totalCommissions = 0.0;

    // Visibilité et état
    private boolean actif = true;
    private boolean premium = false;

    @OneToMany(mappedBy = "fournisseur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ServiceFournisseur> services = new ArrayList<>();
}
