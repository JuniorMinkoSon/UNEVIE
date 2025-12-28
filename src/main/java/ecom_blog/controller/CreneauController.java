package ecom_blog.controller;

import ecom_blog.dto.CreneauDto;
import ecom_blog.service.CreneauService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/creneaux")
@RequiredArgsConstructor
public class CreneauController {

    private final CreneauService creneauService;

    @GetMapping("/produit/{produitId}")
    public ResponseEntity<List<CreneauDto>> getCreneauxProduit(
            @PathVariable Long produitId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        if (debut != null && fin != null) {
            return ResponseEntity.ok(creneauService.searchAvailableSlots(produitId, debut, fin));
        } else {
            return ResponseEntity.ok(creneauService.getCreneauxDisponibles(produitId));
        }
    }

    @PostMapping("/reserver")
    public ResponseEntity<String> reserverCreneau(
            @RequestParam Long creneauId,
            @RequestParam Long commandeId) {
        creneauService.reserverCreneau(creneauId, commandeId);
        return ResponseEntity.ok("Créneau réservé avec succès");
    }

    @PostMapping("/generer")
    public ResponseEntity<String> genererCreneaux(
            @RequestParam Long produitId,
            @RequestParam(defaultValue = "30") int nombreJours) {
        creneauService.genererCreneauxAutomatiques(produitId, nombreJours);
        return ResponseEntity.ok("Créneaux générés avec succès");
    }
}
