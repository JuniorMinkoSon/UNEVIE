package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Politique - Représente les politiques/contrats pour les services
 * Doit être acceptée par le client avant confirmation de commande
 */
@Entity
@Table(name = "politique")
@Getter
@Setter
public class Politique extends BaseEntity {

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenu; // HTML ou Markdown

    @Enumerated(EnumType.STRING)
    private Secteur secteur; // Associé à un secteur spécifique (optionnel)

    @Column(nullable = false)
    private boolean actif = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private ServiceFournisseur service; // Optionnel: politique spécifique à un service

    @Column(length = 500)
    private String description; // Description courte pour l'admin

    // Type de politique
    @Enumerated(EnumType.STRING)
    private TypePolitique type = TypePolitique.GENERALE;
}
