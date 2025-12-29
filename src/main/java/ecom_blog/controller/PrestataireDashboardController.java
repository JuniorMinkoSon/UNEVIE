package ecom_blog.controller;

import ecom_blog.model.Prestataire;
import ecom_blog.model.User;
import ecom_blog.repository.PrestataireRepository;
import ecom_blog.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/prestataire")
public class PrestataireDashboardController {

    private final PrestataireRepository prestataireRepository;
    private final UserService userService;
    private final ecom_blog.repository.CommandeRepository commandeRepository;
    private final ecom_blog.service.TrackingService trackingService;

    public PrestataireDashboardController(PrestataireRepository prestataireRepository,
            UserService userService,
            ecom_blog.repository.CommandeRepository commandeRepository,
            ecom_blog.service.TrackingService trackingService) {
        this.prestataireRepository = prestataireRepository;
        this.userService = userService;
        this.commandeRepository = commandeRepository;
        this.trackingService = trackingService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        Prestataire prestataire = prestataireRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Aucun profil prestataire associé à ce compte"));

        // DEBUG: Lister toutes les commandes pour voir les statuts réels
        java.util.List<ecom_blog.model.Commande> all = commandeRepository.findAll();
        System.out.println("DEBUG DASHBOARD - TOTAL COMMANDES: " + all.size());
        for (ecom_blog.model.Commande c : all) {
            System.out.println("CMD #" + c.getId() + " | Statut: '" + c.getStatut() + "' | Prestataire: "
                    + (c.getPrestataire() == null ? "NULL" : c.getPrestataire().getId()));
        }

        // 1. Charger les offres de mission (Toutes les commandes sans prestataire pour
        // le moment)
        // Correction : On prend tout ce qui est libre pour éviter les problèmes de
        // statut
        java.util.List<ecom_blog.model.Commande> offres = commandeRepository.findByPrestataireIsNull();

        // 2. Charger ma mission en cours (Commandes assignées à moi non livrées)
        java.util.List<ecom_blog.model.Commande> mesMissions = commandeRepository
                .findByPrestataireAndStatutNot(prestataire, "LIVREE");

        // 3. Charger l'historique (Missions livrées)
        java.util.List<ecom_blog.model.Commande> historique = commandeRepository.findByPrestataireAndStatut(prestataire,
                "LIVREE");

        // 4. Calculer les gains du jour (avec sécurité sur la date)
        double gainsDuJour = historique.stream()
                .filter(c -> c.getDateCommande() != null &&
                        c.getDateCommande().toLocalDate().isEqual(java.time.LocalDate.now()))
                .mapToDouble(ecom_blog.model.Commande::getTotal)
                .sum();

        // 5. KPIs
        int coursesTerminees = historique.size();

        model.addAttribute("prestataire", prestataire);
        model.addAttribute("offres", offres);
        model.addAttribute("mesMissions", mesMissions);
        model.addAttribute("historique", historique);
        model.addAttribute("gainsDuJour", gainsDuJour);
        model.addAttribute("coursesTerminees", coursesTerminees);

        return "prestataire/dashboard";
    }

    // ✅ ACCEPTER UNE MISSION
    @PostMapping("/accept/{id}")
    public String acceptMission(@org.springframework.web.bind.annotation.PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(principal.getName());
        Prestataire prestataire = prestataireRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Prestataire introuvable"));

        ecom_blog.model.Commande commande = commandeRepository.findById(id).orElse(null);

        if (commande != null && commande.getPrestataire() == null) {
            // Utiliser le TrackingService pour l'assignation du prestataire connecté
            trackingService.assignerPrestataire(id, prestataire.getId());
            redirectAttributes.addFlashAttribute("success", "Vous avez accepté la mission !");
        } else {
            redirectAttributes.addFlashAttribute("error", "Cette mission n'est plus disponible.");
        }
        return "redirect:/prestataire/dashboard";
    }

    // ❌ ANNULER UNE MISSION
    @PostMapping("/cancel/{id}")
    public String cancelMission(@org.springframework.web.bind.annotation.PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(principal.getName());
        Prestataire prestataire = prestataireRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Prestataire introuvable"));

        ecom_blog.model.Commande commande = commandeRepository.findById(id).orElse(null);

        if (commande != null && commande.getPrestataire() != null
                && commande.getPrestataire().getId().equals(prestataire.getId())) {
            // Utiliser le TrackingService ou logique manuelle. Ici, on garde la logique
            // manuelle pour l'instant
            // mais idéalement tout devrait passer par le service.
            // On va utiliser le service pour le changement de statut si possible, mais le
            // service n'a pas explicitement "annulerMission" qui remet à null.
            // On fait donc manuellement mais proprement.
            commande.setPrestataire(null); // Libérer la commande
            commande.setStatut("EN_ATTENTE"); // Remettre le statut textuel
            commande.setStatutDetaille(ecom_blog.model.enums.StatutCommande.CREEE); // Remettre en attente
            commandeRepository.save(commande);

            // On notifie la dispo
            prestataire.setDisponible(true);
            prestataire.setEnService(false);
            prestataireRepository.save(prestataire);

            redirectAttributes.addFlashAttribute("success",
                    "Mission annulée. Elle est de nouveau disponible pour les autres.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Impossible d'annuler cette mission.");
        }
        return "redirect:/prestataire/dashboard";
    }

    // ✅ TERMINER (LIVRER) UNE MISSION
    @PostMapping("/complete/{id}")
    public String completeMission(@org.springframework.web.bind.annotation.PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        // Via service
        try {
            trackingService.changerStatut(id, ecom_blog.model.enums.StatutCommande.LIVREE);
            redirectAttributes.addFlashAttribute("success", "Félicitations ! Mission terminée.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur : " + e.getMessage());
        }
        return "redirect:/prestataire/dashboard";
    }

    @PostMapping("/status")
    public String toggleStatus(Principal principal, RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(principal.getName());
        Prestataire prestataire = prestataireRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Prestataire introuvable"));

        prestataire.setDisponible(!prestataire.getDisponible());
        prestataireRepository.save(prestataire);

        redirectAttributes.addFlashAttribute("success", "Votre statut a été mis à jour.");
        return "redirect:/prestataire/dashboard";
    }

    @PostMapping("/location")
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<?> updateLocation(@RequestParam Double latitude,
            @RequestParam Double longitude,
            Principal principal) {
        try {
            User user = userService.findByEmail(principal.getName());
            Prestataire prestataire = prestataireRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Prestataire introuvable"));

            // Mise à jour via le service qui diffuse via WebSocket
            trackingService.updatePositionPrestataire(prestataire.getId(), latitude, longitude);

            return org.springframework.http.ResponseEntity.ok().build();
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
