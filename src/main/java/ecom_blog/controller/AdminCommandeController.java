package ecom_blog.controller;

import ecom_blog.model.Commande;
import ecom_blog.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/commandes")
public class AdminCommandeController {

    private final CommandeService commandeService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("commandes", commandeService.getAll());
        return "admin/commandes";
    }

    @PostMapping("/update/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String statut) {

        Commande cmd = commandeService.findById(id);

        if (cmd != null) {
            cmd.setStatut(statut);
            commandeService.save(cmd);
        }

        return "redirect:/admin/commandes";
    }
}
