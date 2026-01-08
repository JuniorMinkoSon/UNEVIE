package ecom_blog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

/**
 * Réponse de l'API Geocoding de Mapbox.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MapboxGeocodingResponse {
    private List<Feature> features;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Feature {
        private String id;
        private String place_name; // Nom complet
        private List<Double> center; // [longitude, latitude]
        private Geometry geometry;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Geometry {
        private String type;
        private List<Double> coordinates;
    }
}
