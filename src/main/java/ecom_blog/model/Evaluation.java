package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Evaluation - Permet aux clients de noter et commenter un service après
 * prestation
 */
@Entity
@Table(name = "evaluation")
@Getter
@Setter
public class Evaluation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceFournisseur service;

    @Column(nullable = false)
    private Integer note; // Note de 1 à 5

    @Column(length = 2000)
    private String commentaire;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id")
    private Commande commande; // Pour évaluations de livraisons produits

    // Photos optionnelles
    @ElementCollection
    @CollectionTable(name = "evaluation_photos", joinColumns = @JoinColumn(name = "evaluation_id"))
    @Column(name = "photo_url")
    private java.util.List<String> photoUrls = new java.util.ArrayList<>();

    private java.time.LocalDateTime dateEvaluation;
}
