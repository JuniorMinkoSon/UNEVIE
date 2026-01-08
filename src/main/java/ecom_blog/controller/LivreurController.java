package ecom_blog.controller;

import ecom_blog.model.Commande;
import ecom_blog.repository.CommandeRepository;
import ecom_blog.service.ItineraireService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/livreur/course")
public class LivreurController {

    private final CommandeRepository commandeRepository;
    private final ItineraireService itineraireService;
    private final ecom_blog.service.UserService userService;

    public LivreurController(CommandeRepository commandeRepository,
            ItineraireService itineraireService,
            ecom_blog.service.UserService userService) {
        this.commandeRepository = commandeRepository;
        this.itineraireService = itineraireService;
        this.userService = userService;
    }

    // DÉTAIL D’UNE COURSE
    @GetMapping("/{id}")
    public String voirCourse(@PathVariable Long id, Model model) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));

        model.addAttribute("commande", commande);
        return "livreur/livreur-course";
    }

    // ACCEPTER
    @PostMapping("/{id}/accepter")
    @Transactional
    public String accepterCourse(@PathVariable Long id,
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {

        Commande commande = commandeRepository.findById(id).orElseThrow();

        // Security check: Order must be accepted by company first
        if (!"ACCEPTEE".equals(commande.getStatut())) {
            throw new RuntimeException(
                    "Cette commande n'est pas disponible pour livraison (Statut: " + commande.getStatut() + ")");
        }

        // Check expiration (5 minutes after provider acceptance)
        if (commande.getDateExpiration() != null
                && java.time.LocalDateTime.now().isAfter(commande.getDateExpiration())) {
            // On pourrait passer le statut à EXPIREE ou juste bloquer
            throw new RuntimeException("Le délai de 5 minutes pour accepter cette course est écoulé.");
        }

        // Assigner le livreur connecté
        if (userDetails != null) {
            userService.findByEmailOptional(userDetails.getUsername())
                    .ifPresent(commande::setLivreur);
        }

        commande.setStatut("EN_COURS_LIVRAISON");

        itineraireService.calculerItineraire(commande);

        return "redirect:/livreur/course/" + id;
    }

    // REFUSER
    @PostMapping("/{id}/refuser")
    @Transactional
    public String refuserCourse(@PathVariable Long id) {
        Commande commande = commandeRepository.findById(id).orElseThrow();
        commande.setStatut("EN_ATTENTE");
        commande.setLivreur(null);

        return "redirect:/livreur/dashboard";
    }
}
