package ecom_blog.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VehiculePositionDto {
    private Long id;
    private Long vehiculeId;
    private Long commandeId;
    private Double latitude;
    private Double longitude;
    private LocalDateTime timestamp;
    private String statut;
    private Double vitesse;
    private Double direction;
    private String adresseApproximative;
}
