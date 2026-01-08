package ecom_blog.dto;

import lombok.Data;

/**
 * PositionUpdateDto - DTO pour mise à jour de position via WebSocket
 * Envoyé par le livreur
 */
@Data
public class PositionUpdateDto {

    private Long commandeId;
    private Double latitude;
    private Double longitude;
    private Double vitesse; // Optionnel
    private Double cap; // Optionnel
    private Double altitude; // Optionnel
    private Double precision; // Optionnel
}
