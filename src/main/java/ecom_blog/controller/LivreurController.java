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

    public LivreurController(CommandeRepository commandeRepository,
                             ItineraireService itineraireService) {
        this.commandeRepository = commandeRepository;
        this.itineraireService = itineraireService;
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
    public String accepterCourse(@PathVariable Long id) {
        Commande commande = commandeRepository.findById(id).orElseThrow();
        commande.setStatut("EN_COURS");

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
