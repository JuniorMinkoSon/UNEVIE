package ecom_blog.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class RouteDto {
    private Long id;
    private Long commandeId;
    private Double distanceTotaleKm;
    private Integer dureeEstimeeMin;
    private List<Map<String, Double>> pathCoordinates; // Liste de {lat, lng} pour affichage map
    private String instructions; // JSON ou texte
    private Long departNodeId;
    private Long arriveeNodeId;
}
