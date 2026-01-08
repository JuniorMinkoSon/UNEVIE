package ecom_blog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * MapboxDirectionsResponse - Réponse de l'API Mapbox Directions
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MapboxDirectionsResponse {

    private List<Route> routes;
    private String code;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Route {
        private Double distance; // En mètres
        private Double duration; // En secondes
        private Geometry geometry;
        private List<Leg> legs;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Geometry {
        private String type;
        private List<List<Double>> coordinates; // [longitude, latitude]
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Leg {
        private Double distance;
        private Double duration;
        private List<Step> steps;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Step {
        private Double distance;
        private Double duration;
        private Geometry geometry;
        private String name;
    }
}
