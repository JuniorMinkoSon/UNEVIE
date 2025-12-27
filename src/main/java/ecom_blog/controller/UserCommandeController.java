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
}
