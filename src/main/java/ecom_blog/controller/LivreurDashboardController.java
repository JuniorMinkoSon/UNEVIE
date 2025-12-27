package ecom_blog.controller;

import ecom_blog.repository.CommandeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/livreur/dashboard")
public class LivreurDashboardController {

    private final CommandeRepository commandeRepository;

    public LivreurDashboardController(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("commandes", commandeRepository.findAll());
        return "livreur/dashboard";
    }
}
