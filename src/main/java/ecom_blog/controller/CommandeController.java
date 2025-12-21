package ecom_blog.controller;

import ecom_blog.model.Commande;
import ecom_blog.model.Produit;
import ecom_blog.security.CustomUserDetails;
import ecom_blog.service.CommandeService;
import ecom_blog.service.ProduitService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommandeController {

    private final ProduitService produitService;
    private final CommandeService commandeService;

    public CommandeController(ProduitService produitService,
                              CommandeService commandeService) {
        this.produitService = produitService;
        this.commandeService = commandeService;
    }

    // 📄 AFFICHER LE FORMULAIRE DE COMMANDE
    @GetMapping("/commande/{id}")
    public String afficherFormulaire(@PathVariable Long id, Model model) {

        Produit produit = produitService.getById(id);

        model.addAttribute("produit", produit);
        return "user/commande-form";
    }

    // 🛒 VALIDER LA COMMANDE
    @PostMapping("/commande/valider")
    public String validerCommande(@RequestParam Long produitId,
                                  @RequestParam String modePaiement,
                                  @AuthenticationPrincipal CustomUserDetails userDetails) {

        Produit produit = produitService.getById(produitId);

        Commande commande = new Commande();
        commande.setProduit(produit);
        commande.setTotal(produit.getPrix());
        commande.setModePaiement(modePaiement);

        // 👤 Si utilisateur connecté
        if (userDetails != null) {
            commande.setUser(userDetails.getUser());
        }

        commandeService.save(commande);

        return "redirect:/projets?success=commande";
    }
}
