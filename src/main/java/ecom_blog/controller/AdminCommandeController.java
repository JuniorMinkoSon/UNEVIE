package ecom_blog.controller;

import ecom_blog.model.Commande;
import ecom_blog.service.CommandeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/commandes")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCommandeController {

    private final CommandeService commandeService;

    public AdminCommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    // 📦 LISTE DES COMMANDES
    @GetMapping
    public String listeCommandes(Model model) {
        model.addAttribute("commandes", commandeService.getAll());
        return "admin/Commandes"; // ⚠️ respecter la casse exacte du fichier
    }

    // 🔄 MISE À JOUR DU STATUT
    @PostMapping("/update/{id}")
    public String updateStatut(@PathVariable Long id,
                               @RequestParam String statut) {

        Commande commande = commandeService.getById(id);

        if (commande != null) {
            commande.setStatut(statut);
            commandeService.save(commande);
        }

        return "redirect:/admin/commandes";
    }
}
