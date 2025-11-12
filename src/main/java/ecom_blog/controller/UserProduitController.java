package ecom_blog.controller;

import ecom_blog.model.Commande;
import ecom_blog.model.Produit;
import ecom_blog.model.User;
import ecom_blog.service.ArticleService;
import ecom_blog.service.CommandeService;
import ecom_blog.service.ProduitService;
import ecom_blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
@Controller
public class UserProduitController {

    @Autowired
    private ProduitService produitService;

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    // ✅ Route principale qui affiche articles + produits
    @GetMapping("/projets")
    public String projets(Model model) {
        model.addAttribute("produits", produitService.getAllDisponibles());
        model.addAttribute("articles", articleService.getAll());
        return "user/projets";
        // 🔥 le nom du fichier sans extension
    }

    @PostMapping("/produits/commander/{id}")
    public String commanderProduit(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login?redirect=projets";
        }

        Produit produit = produitService.getById(id);
        if (produit == null || !produit.isDisponible()) {
            return "redirect:/projets?error=notfound";
        }

        User user = userService.findByEmail(principal.getName());
        Commande commande = new Commande();
        commande.setProduit(produit);
        commande.setUser(user);
        commande.setDateCommande(LocalDateTime.now());
        commande.setTotal(produit.getPrix());
        commande.setStatut("EN_ATTENTE");

        commandeService.save(commande);
        return "redirect:/projets?success=commande";
    }
}
