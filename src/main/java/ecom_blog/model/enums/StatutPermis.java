package ecom_blog.model.enums;

public enum StatutPermis {
    EN_ATTENTE, // Document uploadé, en attente de vérification
    VALIDE, // Document vérifié et validé par l'admin
    REFUSE // Document refusé (invalide, illisible, expiré, etc.)
}
