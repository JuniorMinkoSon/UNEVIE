package ecom_blog.controller;

import ecom_blog.model.Commande;
import ecom_blog.model.enums.StatutCommande;
import ecom_blog.service.TrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tracking")
public class TrackingController {

    private final TrackingService trackingService;

    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    /**
     * Récupère les informations de suivi d'une commande
     */
    @GetMapping("/{commandeId}")
    public ResponseEntity<Map<String, Object>> getTrackingInfo(@PathVariable Long commandeId) {
        try {
            Map<String, Object> info = trackingService.getTrackingInfo(commandeId);
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Assigne un prestataire à une commande
     */
    @PostMapping("/{commandeId}/assigner-prestataire")
    public ResponseEntity<?> assignerPrestataire(@PathVariable Long commandeId) {
        try {
            Commande commande = trackingService.assignerPrestataire(commandeId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Prestataire assigné avec succès",
                    "commande", commande));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()));
        }
    }

    /**
     * Met à jour la position d'un prestataire
     */
    @PutMapping("/prestataire/{prestataireId}/position")
    public ResponseEntity<?> updatePosition(
            @PathVariable Long prestataireId,
            @RequestParam Double latitude,
            @RequestParam Double longitude) {
        try {
            trackingService.updatePositionPrestataire(prestataireId, latitude, longitude);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Position mise à jour"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()));
        }
    }

    /**
     * Change le statut d'une commande
     */
    @PutMapping("/{commandeId}/statut")
    public ResponseEntity<?> changerStatut(
            @PathVariable Long commandeId,
            @RequestParam String statut) {
        try {
            StatutCommande nouveauStatut = StatutCommande.valueOf(statut);
            Commande commande = trackingService.changerStatut(commandeId, nouveauStatut);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Statut mis à jour",
                    "commande", commande));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()));
        }
    }

    /**
     * Démarre la simulation de déplacement (pour tests)
     */
    @PostMapping("/{commandeId}/simuler-deplacement")
    public ResponseEntity<?> simulerDeplacement(@PathVariable Long commandeId) {
        try {
            trackingService.simulerDeplacementPrestataire(commandeId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Simulation démarrée"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", e.getMessage()));
        }
    }
}
