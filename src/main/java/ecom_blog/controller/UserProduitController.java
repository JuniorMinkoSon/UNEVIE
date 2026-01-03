package ecom_blog.controller;

import ecom_blog.model.Produit;
import ecom_blog.service.ArticleService;
import ecom_blog.service.CategorieService;
import ecom_blog.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserProduitController {

    @Autowired
    private ProduitService produitService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategorieService categorieService;

    /**
     */
    @GetMapping("/projets")
    public String projets(@RequestParam(required = false) String categorie, Model model) {
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
        model.addAttribute("categories", categorieService.findAll());
        model.addAttribute("articles", articleService.getAll());
        return "user/projets";
    }
}
