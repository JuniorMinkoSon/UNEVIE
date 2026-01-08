package ecom_blog.controller;

import ecom_blog.model.User;
import ecom_blog.model.Commande;
import ecom_blog.service.CommandeService;
import ecom_blog.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserCommandeController {

    private final CommandeService commandeService;

    public UserCommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    @GetMapping("/mes-commandes")
    public String mesCommandes(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login"; // sécurité si non connecté
        }

        User user = userDetails.getUser();
        List<Commande> commandes = commandeService.getByUser(user);

        if (commandes == null) {
            commandes = new ArrayList<>();
        }

        model.addAttribute("commandes", commandes);
        return "user/mes-commandes"; // Chemin correct du template
    }

    @GetMapping("/suivi-commande/{id}")
    public String suiviCommande(@org.springframework.web.bind.annotation.PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Commande commande = commandeService.findById(id);

        if (commande == null) {
            return "redirect:/mes-commandes";
        }

        // Vérifier que la commande appartient bien à l'utilisateur
        if (!commande.getUser().getId().equals(userDetails.getUser().getId())) {
            return "redirect:/mes-commandes";
        }

        model.addAttribute("commande", commande);
        return "user/suivi-commande";
    }

    @org.springframework.web.bind.annotation.PostMapping("/commande/annuler/{id}")
    public String annulerCommande(@org.springframework.web.bind.annotation.PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        Commande commande = commandeService.findById(id);

        if (commande != null && commande.getUser().getId().equals(userDetails.getUser().getId())) {
            // Vérifier si la commande peut être annulée (ex: seulement si EN_ATTENTE)
            if ("EN_ATTENTE".equals(commande.getStatut())) {
                commande.setStatut("ANNULEE");
                commandeService.save(commande);
                redirectAttributes.addFlashAttribute("successMessage", "La commande a été annulée avec succès.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Impossible d'annuler cette commande.");
            }
        }

        return "redirect:/mes-commandes";
    }
}
