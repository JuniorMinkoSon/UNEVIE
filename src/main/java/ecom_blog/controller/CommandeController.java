package ecom_blog.controller;

import ecom_blog.model.Commande;
import ecom_blog.model.Produit;
import ecom_blog.model.User;
import ecom_blog.service.CommandeService;
import ecom_blog.service.ProduitService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/panier")
public class CommandeController {

    @Autowired
    private ProduitService produitService;

    @Autowired
    private CommandeService commandeService;

    @PostMapping("/ajouter/{id}")
    public String ajouterPanier(@PathVariable Long id, HttpSession session) {
        Produit produit = produitService.findById(id);
        if (produit == null) return "redirect:/";

        List<Produit> panier = (List<Produit>) session.getAttribute("panier");
        if (panier == null) panier = new ArrayList<>();

        panier.add(produit);
        session.setAttribute("panier", panier);

        return "redirect:/panier";
    }

    @GetMapping
    public String afficherPanier(Model model, HttpSession session) {
        List<Produit> panier = (List<Produit>) session.getAttribute("panier");
        double total = panier == null ? 0 : panier.stream().mapToDouble(Produit::getPrix).sum();

        model.addAttribute("panier", panier);
        model.addAttribute("total", total);
        return "user/panier"; // Vue user/panier.html
    }

    @PostMapping("/valider")
    public String validerCommande(HttpSession session, @RequestParam String modePaiement) {
        List<Produit> panier = (List<Produit>) session.getAttribute("panier");
        if (panier == null || panier.isEmpty()) {
            return "redirect:/panier?error=vide";
        }

        Commande commande = new Commande();
        commande.setTotal(panier.stream().mapToDouble(Produit::getPrix).sum());
        commande.setUser((User) session.getAttribute("user"));
        commande.setModePaiement(modePaiement);
        commande.setStatut("EN_ATTENTE");

        commandeService.save(commande);
        session.removeAttribute("panier");

        return "redirect:/confirmation";
    }
}
