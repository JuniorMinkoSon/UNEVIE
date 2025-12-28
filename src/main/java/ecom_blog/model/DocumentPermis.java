package ecom_blog.model;

import ecom_blog.model.enums.StatutPermis;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents_permis")
@Getter
@Setter
public class DocumentPermis extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    @Column(nullable = false)
    private String cheminFichier; // Chemin vers le fichier uploadé

    private String numeroPermis; // Numéro du permis (optionnel)

    private LocalDate dateExpiration; // Date d'expiration du permis

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutPermis statut = StatutPermis.EN_ATTENTE;

    @Column(nullable = false)
    private LocalDateTime dateUpload = LocalDateTime.now();

    private LocalDateTime dateVerification; // Date de validation/refus par admin

    @ManyToOne
    @JoinColumn(name = "verifie_par")
    private User verifiepar; // Admin qui a vérifié le document

    @Column(length = 500)
    private String commentaireRefus; // Si refusé, raison du refus
}
