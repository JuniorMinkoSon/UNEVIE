package ecom_blog.controller;

import ecom_blog.dto.CourierStatusUpdateDto;
import ecom_blog.dto.LivreurDto;
import ecom_blog.dto.PositionUpdateDto;
import ecom_blog.dto.TrackingDto;
import ecom_blog.model.Commande;
import ecom_blog.model.User;
import ecom_blog.repository.CommandeRepository;
import ecom_blog.repository.UserRepository;
import ecom_blog.service.TrackingService;
import ecom_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TrackingController - Gère le tracking GPS via WebSocket et REST
 */
@Controller
public class TrackingController {

    @Autowired
    private TrackingService trackingService;

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    /**
     * Endpoint WebSocket pour mise à jour position livreur
     * Le livreur envoie sa position toutes les 5 secondes
     */
    @MessageMapping("/livreur/position")
    public void updatePosition(@Payload PositionUpdateDto dto, Principal principal) {
        try {
            // Récupérer le livreur connecté
            User livreur = userService.findByEmail(principal.getName());

            // Récupérer la commande
            Commande commande = commandeRepository.findById(dto.getCommandeId())
                    .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

            // Enregistrer et diffuser la position
            trackingService.enregistrerPosition(
                    livreur,
                    commande,
                    dto.getLatitude(),
                    dto.getLongitude(),
                    dto.getVitesse(),
                    dto.getCap());

        } catch (Exception e) {
            // Log error
            e.printStackTrace();
        }
    }

    /**
     * REST endpoint pour récupérer le tracking actuel d'une commande
     * Utilisé par le client pour afficher la carte
     */
    @GetMapping("/api/tracking/commande/{id}")
    @ResponseBody
    public TrackingDto getTracking(@PathVariable Long id) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        return trackingService.getTrackingActuel(commande);
    }

    /**
     * Endpoint pour démarrer le tracking d'une commande
     */
    @PostMapping("/api/tracking/commande/{id}/demarrer")
    @ResponseBody
    public String demarrerTracking(@PathVariable Long id, Authentication auth) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        // Vérifier que c'est bien le livreur assigné
        User livreur = userService.findByEmail(auth.getName());
        if (!commande.getLivreur().getId().equals(livreur.getId())) {
            return "error";
        }

        // Mettre à jour le statut
        commande.setStatut(ecom_blog.model.StatutCommande.EN_COURS_LIVRAISON.name());
        commande.setDebutLivraison(System.currentTimeMillis());
        commandeRepository.save(commande);

        return "success";
    }

    /**
     * Endpoint pour terminer une livraison
     */
    @PostMapping("/api/tracking/commande/{id}/terminer")
    @ResponseBody
    public String terminerLivraison(@PathVariable Long id, Authentication auth) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        // Vérifier que c'est bien le livreur assigné
        User livreur = userService.findByEmail(auth.getName());
        if (!commande.getLivreur().getId().equals(livreur.getId())) {
            return "error";
        }

        // Mettre à jour le statut
        commande.setStatut(ecom_blog.model.StatutCommande.LIVREE.name());
        commande.setDateExecution(java.time.LocalDateTime.now());
        commandeRepository.save(commande);

        return "success";
    }

    /**
     * REST endpoint pour récupérer tous les livreurs disponibles (en ligne)
     */
    @GetMapping("/api/tracking/couriers/available")
    @ResponseBody
    public List<LivreurDto> getAvailableCouriers() {
        return userService.getDisponibleLivreurs().stream()
                .map(u -> new LivreurDto(u.getId(), u.getNom(), u.getLatitude(), u.getLongitude()))
                .collect(Collectors.toList());
    }

    /**
     * Endpoint pour mettre à jour le statut/position du livreur en arrière-plan
     */
    @PostMapping("/api/tracking/livreur/status")
    @ResponseBody
    public String updateLivreurStatus(@RequestBody CourierStatusUpdateDto dto, Authentication auth) {
        try {
            User livreur = userService.findByEmail(auth.getName());
            if (livreur != null) {
                livreur.setLatitude(dto.getLatitude());
                livreur.setLongitude(dto.getLongitude());
                userRepository.save(livreur);
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
}
