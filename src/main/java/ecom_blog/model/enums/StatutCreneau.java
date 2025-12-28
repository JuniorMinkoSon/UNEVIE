package ecom_blog.model.enums;

public enum StatutCreneau {
    DISPONIBLE, // Créneau libre et réservable
    RESERVE, // Créneau réservé par une commande
    BLOQUE // Créneau bloqué (maintenance, indisponibilité, etc.)
}
