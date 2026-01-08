package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * CreneauHoraire - Gère les créneaux de disponibilité des fournisseurs
 * Permet aux clients de réserver des plages horaires spécifiques
 */
@Entity
@Table(name = "creneau_horaire")
@Getter
@Setter
public class CreneauHoraire extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private User fournisseur;

    @Column(nullable = false)
    private LocalDateTime debut;

    @Column(nullable = false)
    private LocalDateTime fin;

    @Column(nullable = false)
    private boolean disponible = true;

    @Column(nullable = false)
    private boolean reserve = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id")
    private Commande commande; // Si réservé

    // ================== RÉCURRENCE (optionnel) ==================
    // Pour créneaux répétitifs (ex: tous les lundis 9h-12h)

    @Enumerated(EnumType.STRING)
    private JourSemaine jourSemaine; // LUNDI, MARDI, etc.

    private LocalTime heureDebut;
    private LocalTime heureFin;

    @Column(nullable = false)
    private boolean recurrent = false; // Si true, se répète chaque semaine
}
