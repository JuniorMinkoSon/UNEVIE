package ecom_blog.controller;

import ecom_blog.service.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class FrontProduitController {

    private final ProduitService produitService;

    @GetMapping("/produits")
    public String afficherProduits(Model model) {

        model.addAttribute("produits", produitService.getAll());

        return "user/projets";  // le fichier projets.html
    }
}
