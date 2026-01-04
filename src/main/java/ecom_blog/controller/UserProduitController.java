package ecom_blog.controller;

import ecom_blog.model.Produit;
import ecom_blog.model.ServiceFournisseur;
import ecom_blog.model.Secteur;
import ecom_blog.service.ArticleService;
import ecom_blog.service.CategorieService;
import ecom_blog.service.ProduitService;
import ecom_blog.service.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserProduitController {

    @Autowired
    private ProduitService produitService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategorieService categorieService;

    @Autowired
    private FournisseurService fournisseurService;

    /**
     */
    @GetMapping("/projets")
    public String projets(@RequestParam(required = false) String categorie, Model model) {
        List<Produit> produits;
        List<ServiceFournisseur> services;

        if (categorie == null || categorie.isBlank()) {
            produits = produitService.getAllDisponibles();
            services = fournisseurService.getAllServices().stream()
                    .filter(ServiceFournisseur::isDisponible)
                    .collect(Collectors.toList());
        } else {
            // Filtrage par catégorie de produit
            produits = produitService.getAllDisponibles()
                    .stream()
                    .filter(p -> p.getCategorie() != null &&
                            p.getCategorie().equalsIgnoreCase(categorie))
                    .collect(Collectors.toList());

            // Filtrage par secteur de service
            services = fournisseurService.getAllServices().stream()
                    .filter(s -> s.isDisponible() &&
                            (s.getSecteur().name().equalsIgnoreCase(categorie) ||
                                    s.getSecteur().toString().equalsIgnoreCase(categorie)))
                    .collect(Collectors.toList());
        }

        model.addAttribute("produits", produits);
        model.addAttribute("services", services);
        model.addAttribute("categories", categorieService.findAll());
        model.addAttribute("secteurs", Secteur.values());
        model.addAttribute("articles", articleService.getAll());
        return "user/projets";
    }
}
