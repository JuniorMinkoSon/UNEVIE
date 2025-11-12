package ecom_blog.controller;

import ecom_blog.model.Commande;
import ecom_blog.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/commandes")
public class AdminCommandeController {

    @Autowired
    private CommandeService commandeService;

    // ✅ Liste complète des commandes
    @GetMapping
    public String listeCommandes(Model model) {
        model.addAttribute("commandes", commandeService.getAll());
        return "admin/commandes"; // templates/admin/commandes.html
    }

    // ✅ Mise à jour du statut d'une commande
    @PostMapping("/update/{id}")
    public String updateStatut(@PathVariable Long id, @RequestParam String statut) {
        Commande commande = commandeService.getById(id);
        if (commande != null) {
            commande.setStatut(statut);
            commandeService.save(commande);
        }
        return "redirect:/admin/commandes?success";
    }
}
