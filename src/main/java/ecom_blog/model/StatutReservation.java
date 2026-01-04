package ecom_blog.model;

/**
 * Statuts possibles d'une réservation
 */
public enum StatutReservation {
    EN_COURS, // Réservation soumise, en attente de réponse du fournisseur
    ACCEPTE, // Fournisseur a accepté (commission 5% appliquée)
    REFUSE, // Fournisseur a refusé
    TERMINE // Service effectué
}
