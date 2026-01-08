package ecom_blog.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * TrackingDto - DTO pour les données de tracking GPS
 * Envoyé via WebSocket aux clients
 */
@Data
public class TrackingDto {

    private Double latitude;
    private Double longitude;
    private Long eta; // Temps estimé d'arrivée en secondes
    private LocalDateTime timestamp;

    // Informations livreur (optionnel)
    private String livreurNom;
    private String livreurTelephone;
    private String livreurPhoto;

    // Données supplémentaires
    private Double vitesse; // km/h
    private Double cap; // Direction en degrés
    private Double distanceRestante; // En mètres
}
