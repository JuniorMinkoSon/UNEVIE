package ecom_blog.service;

import ecom_blog.dto.MapboxDirectionsResponse;
import ecom_blog.dto.MapboxGeocodingResponse;
import reactor.core.publisher.Mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * MapboxService - Service pour interagir avec l'API Mapbox
 * Calcule les itinéraires et ETA
 */
@Service
public class MapboxService {

    private static final Logger logger = LoggerFactory.getLogger(MapboxService.class);

    @Value("${mapbox.access.token}")
    private String accessToken;

    @Value("${mapbox.api.url}")
    private String apiUrl;

    private final WebClient webClient;

    public MapboxService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Calcule l'itinéraire entre deux points
     * 
     * @param lonDepart  Longitude de départ
     * @param latDepart  Latitude de départ
     * @param lonArrivee Longitude d'arrivée
     * @param latArrivee Latitude d'arrivée
     * @return Réponse Mapbox avec itinéraire
     */
    public MapboxDirectionsResponse getDirections(
            double lonDepart, double latDepart,
            double lonArrivee, double latArrivee) {

        String url = String.format(java.util.Locale.US,
                "%s/directions/v5/mapbox/driving/%f,%f;%f,%f",
                apiUrl, lonDepart, latDepart, lonArrivee, latArrivee);

        logger.debug("Appel Mapbox Directions API: {} -> {}",
                new double[] { lonDepart, latDepart },
                new double[] { lonArrivee, latArrivee });

        try {
            Mono<MapboxDirectionsResponse> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(url.replace(apiUrl, ""))
                            .queryParam("access_token", accessToken)
                            .queryParam("geometries", "geojson")
                            .queryParam("overview", "full")
                            .queryParam("steps", "true")
                            .build())
                    .retrieve()
                    .bodyToMono(MapboxDirectionsResponse.class);

            return response.block();

        } catch (Exception e) {
            logger.error("Erreur lors de l'appel Mapbox API", e);
            return null;
        }
    }

    /**
     * Calcule l'ETA (temps estimé d'arrivée) en secondes
     */
    public long calculerETA(double lonDepart, double latDepart,
            double lonArrivee, double latArrivee) {

        MapboxDirectionsResponse response = getDirections(
                lonDepart, latDepart, lonArrivee, latArrivee);

        if (response != null &&
                response.getRoutes() != null &&
                !response.getRoutes().isEmpty()) {

            Double duration = response.getRoutes().get(0).getDuration();
            logger.debug("ETA calculé: {} secondes", duration);
            return duration != null ? duration.longValue() : 0;
        }

        logger.warn("Impossible de calculer l'ETA");
        return 0;
    }

    /**
     * Calcule la distance en mètres
     */
    public double calculerDistance(double lonDepart, double latDepart,
            double lonArrivee, double latArrivee) {

        MapboxDirectionsResponse response = getDirections(
                lonDepart, latDepart, lonArrivee, latArrivee);

        if (response != null &&
                response.getRoutes() != null &&
                !response.getRoutes().isEmpty()) {

            Double distance = response.getRoutes().get(0).getDistance();
            logger.debug("Distance calculée: {} mètres", distance);
            return distance != null ? distance : 0;
        }

        logger.warn("Impossible de calculer la distance");
        return 0;
    }

    /**
     * Récupère la géométrie de l'itinéraire (pour affichage sur carte)
     */
    public MapboxDirectionsResponse.Geometry getRouteGeometry(
            double lonDepart, double latDepart,
            double lonArrivee, double latArrivee) {

        MapboxDirectionsResponse response = getDirections(
                lonDepart, latDepart, lonArrivee, latArrivee);

        if (response != null &&
                response.getRoutes() != null &&
                !response.getRoutes().isEmpty()) {

            return response.getRoutes().get(0).getGeometry();
        }

        return null; // Missing return for getRouteGeometry
    }

    /**
     * Géocode une adresse (convertit adresse -> [long, lat])
     * 
     * @param adresse Adresse à géocoder
     * @return Tableau [longitude, latitude] ou null si non trouvé
     */
    public double[] geocodeAddress(String adresse) {
        if (adresse == null || adresse.isEmpty()) {
            return null;
        }

        try {
            logger.debug("Geocoding address: {}", adresse);

            Mono<MapboxGeocodingResponse> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/geocoding/v5/mapbox.places/{adresse}.json")
                            .queryParam("access_token", accessToken)
                            .queryParam("limit", "1")
                            .build(adresse))
                    .retrieve()
                    .bodyToMono(MapboxGeocodingResponse.class);

            MapboxGeocodingResponse geoResponse = response.block();

            if (geoResponse != null && geoResponse.getFeatures() != null && !geoResponse.getFeatures().isEmpty()) {
                MapboxGeocodingResponse.Feature feature = geoResponse.getFeatures().get(0);
                if (feature.getCenter() != null && feature.getCenter().size() >= 2) {
                    double lon = feature.getCenter().get(0);
                    double lat = feature.getCenter().get(1);
                    return new double[] { lon, lat };
                }
            }

            return null;

        } catch (Exception e) {
            logger.error("Erreur géocodage adresse: {}", adresse, e);
            return null;
        }
    }
}
