package ecom_blog.model;

public enum StatutCommande {
    EN_ATTENTE, // En attente d'acceptation admin
    EXPIREE, // Timer 10min dépassé
    REDIRIGEE, // Redirigée vers autre entreprise
    ASSIGNEE, // Livreur assigné
    ACCEPTEE, // Livreur a accepté
    EN_COURS_LIVRAISON, // Livraison en cours (GPS actif)
    LIVREE, // Livrée
    EVALUEE, // Client a évalué
    ANNULEE // Annulée
}
