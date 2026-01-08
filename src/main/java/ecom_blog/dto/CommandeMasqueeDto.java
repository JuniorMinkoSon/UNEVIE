package ecom_blog.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * CommandeMasqueeDto - DTO pour commandes avec données sensibles masquées
 * Utilisé pour afficher les commandes aux fournisseurs avant acceptation
 */
@Data
public class CommandeMasqueeDto {

    private Long id;

    // Données masquées
    private String nomClient; // Masqué: "J. D."
    private String telephone; // Masqué: "XXX-XXX-1234"
    private String adresse; // Masqué: "Dakar Plateau" (seulement quartier/ville)

    // Données non sensibles
    private Double total;
    private LocalDateTime dateCommande;
    private LocalDateTime dateCreneauDebut;
    private LocalDateTime dateCreneauFin;
    private String statut;
    private long tempsRestant; // En secondes avant expiration
    private String produitNom;
    private int quantite;

    // Indicateur
    private boolean donneesVisibles; // false si masquées
}
