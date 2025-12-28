package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "produit")
@Getter
@Setter
public class Produit extends BaseEntity {

    private String nom;
    private String categorie;
    private Double prix;
    private String description;
    private String imageUrl;
    private boolean disponible = true;

    // 🏢 Relation vers l'entreprise propriétaire
    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;

    // 📍 Quartier spécifique (si différent du siège de l'entreprise)
    @ManyToOne
    @JoinColumn(name = "quartier_id")
    private Quartier quartier;

    // 🚗 Indique si ce produit nécessite un permis de conduire (pour véhicules)
    @Column(nullable = false)
    private Boolean requiresPermis = false;

    // 📅 Relation vers les créneaux de disponibilité
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    private List<CreneauDisponibilite> creneaux;

    public boolean isDisponible() {
        return disponible;
    }

}
