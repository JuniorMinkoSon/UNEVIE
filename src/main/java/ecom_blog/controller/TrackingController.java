package ecom_blog.controller;

import ecom_blog.dto.VehiculePositionDto;
import ecom_blog.model.VehiculePosition;
import ecom_blog.model.enums.StatutVehicule;
import ecom_blog.service.VehiculeTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller pour gérer le suivi des véhicules (REST)
 */
@RestController
@RequestMapping("/api/tracking")
@RequiredArgsConstructor
public class TrackingController {

    private final VehiculeTrackingService trackingService;

    /**
     * Récupère la dernière position de tous les véhicules actifs
     */
    @GetMapping("/active")
    public ResponseEntity<List<VehiculePositionDto>> getActiveVehicles() {
        List<VehiculePosition> positions = trackingService.getAllLatestPositions();
        return ResponseEntity.ok(positions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
    }

    /**
     * Récupère l'historique d'un véhicule
     */
    @GetMapping("/history/{vehiculeId}")
    public ResponseEntity<List<VehiculePositionDto>> getHistory(
            @PathVariable Long vehiculeId,
            @RequestParam(required = false) Integer hours) {

        LocalDateTime debut = LocalDateTime.now().minusHours(hours != null ? hours : 24);
        LocalDateTime fin = LocalDateTime.now();

        List<VehiculePosition> history = trackingService.getPositionHistory(vehiculeId, debut, fin);
        return ResponseEntity.ok(history.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
    }

    /**
     * Endpoint pour simuler/mettre à jour une position (appelé par l'app chauffeur
     * ou simulateur)
     */
    @PostMapping("/update")
    public ResponseEntity<VehiculePositionDto> updatePosition(@RequestBody VehiculePositionDto dto) {
        VehiculePosition updated = trackingService.updatePosition(
                dto.getVehiculeId(),
                dto.getLatitude(),
                dto.getLongitude(),
                StatutVehicule.valueOf(dto.getStatut()),
                dto.getVitesse(),
                dto.getDirection());
        return ResponseEntity.ok(convertToDto(updated));
    }

    // Convertisseur simple Entity -> DTO
    private VehiculePositionDto convertToDto(VehiculePosition entity) {
        VehiculePositionDto dto = new VehiculePositionDto();
        dto.setId(entity.getId());
        dto.setVehiculeId(entity.getVehiculeId());
        dto.setCommandeId(entity.getCommandeId());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setTimestamp(entity.getTimestamp());
        dto.setStatut(entity.getStatut().name());
        dto.setVitesse(entity.getVitesse());
        dto.setDirection(entity.getDirection());
        dto.setAdresseApproximative(entity.getAdresseApproximative());
        return dto;
    }
}
