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
import java.util.List;

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

    /**
     * Page projets / produits avec filtre par catégorie
     */
    @GetMapping("/projets")
    public String projets(
            @RequestParam(required = false) String categorie,
            Model model
    ) {
        List<Produit> produits;

        if (categorie == null || categorie.isBlank()) {
            produits = produitService.getAllDisponibles();
        } else {
            produits = produitService.getAllDisponibles()
                    .stream()
                    .filter(p -> p.getCategorie() != null &&
                            p.getCategorie().equalsIgnoreCase(categorie))
                    .toList();
        }

        model.addAttribute("produits", produits);
        model.addAttribute("articles", articleService.getAll());
        return "user/projets";
    }

    /**
     * Commander un produit (POST obligatoire)
     */
    @PostMapping("/produits/commander/{id}")
    public String commanderProduit(
            @PathVariable Long id,
            Principal principal
    ) {
        if (principal == null) {
            return "redirect:/login";
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
