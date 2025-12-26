package ecom_blog.model.enums;

public enum StatutCommande {
    CREEE, // Commande créée
    RECHERCHE_PRESTATAIRE, // Recherche d'un prestataire en cours
    PRESTATAIRE_ASSIGNE, // Prestataire trouvé et assigné
    EN_ROUTE, // Le prestataire est en route
    ARRIVEE_IMMINENTE, // Le prestataire arrive (< 5 min)
    LIVREE, // Commande livrée/service effectué
    ANNULEE // Commande annulée
}
