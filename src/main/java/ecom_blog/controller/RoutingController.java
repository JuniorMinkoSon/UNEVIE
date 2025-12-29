package ecom_blog.controller;

import ecom_blog.dto.RouteDto;
import ecom_blog.model.RoadNode;
import ecom_blog.service.DijkstraRoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller pour le calcul d'itinéraires et l'optimisation
 */
@RestController
@RequestMapping("/api/routing")
@RequiredArgsConstructor
public class RoutingController {

    private final DijkstraRoutingService routingService;

    /**
     * Calcule le chemin le plus court entre deux points GPS
     */
    @PostMapping("/calculate")
    public ResponseEntity<RouteDto> calculateRoute(@RequestBody Map<String, Map<String, Double>> payload) {
        Map<String, Double> depart = payload.get("depart");
        Map<String, Double> arrivee = payload.get("arrivee");

        if (depart == null || arrivee == null) {
            return ResponseEntity.badRequest().build();
        }

        List<RoadNode> path = routingService.calculatePathFromCoordinates(
                depart.get("lat"), depart.get("lng"),
                arrivee.get("lat"), arrivee.get("lng"));

        if (path == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertToDto(path));
    }

    private RouteDto convertToDto(List<RoadNode> path) {
        RouteDto dto = new RouteDto();

        dto.setDistanceTotaleKm(routingService.calculateTotalDistance(path));
        dto.setDureeEstimeeMin(routingService.calculateTotalDuration(path));
        dto.setPathCoordinates(routingService.convertPathToCoordinates(path));

        if (!path.isEmpty()) {
            dto.setDepartNodeId(path.get(0).getId());
            dto.setArriveeNodeId(path.get(path.size() - 1).getId());
        }

        return dto;
    }
}
