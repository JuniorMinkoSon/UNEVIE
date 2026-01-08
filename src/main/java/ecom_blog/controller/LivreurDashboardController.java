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
    private final ecom_blog.service.UserService userService;

    public LivreurDashboardController(CommandeRepository commandeRepository,
            ecom_blog.service.UserService userService) {
        this.commandeRepository = commandeRepository;
        this.userService = userService;
    }

    @GetMapping
    public String dashboard(Model model) {
        // Only show orders that have been accepted by the company (Admin)
        model.addAttribute("commandes", commandeRepository.findByStatutOrderByDateCommandeDesc("ACCEPTEE"));
        return "livreur/dashboard";
    }

    @GetMapping("/mes-livraisons")
    public String mesLivraisons(Model model, org.springframework.security.core.Authentication auth) {
        // Récupérer le livreur connecté
        ecom_blog.model.User livreur = userService.findByEmail(auth.getName());

        // Récupérer les commandes assignées à ce livreur
        java.util.List<ecom_blog.model.Commande> commandes = commandeRepository
                .findByLivreurOrderByDateCommandeDesc(livreur);

        model.addAttribute("commandes", commandes);
        return "livreur/mes-livraisons";
    }
}
