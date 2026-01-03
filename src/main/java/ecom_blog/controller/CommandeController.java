package ecom_blog.controller;

import ecom_blog.model.Commande;
import ecom_blog.model.Produit;
import ecom_blog.security.CustomUserDetails;
import ecom_blog.service.CommandeService;
import ecom_blog.service.ProduitService;

import java.time.LocalDateTime;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommandeController {

    private final ProduitService produitService;
    private final CommandeService commandeService;

    public CommandeController(ProduitService produitService, CommandeService commandeService) {
        this.produitService = produitService;
        this.commandeService = commandeService;
    }

    // 📄 AFFICHER LE FORMULAIRE DE COMMANDE
    @GetMapping("/commande/{id}")
    public String afficherFormulaire(@PathVariable Long id, Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Produit produit = produitService.getById(id);
        if (produit == null) {
            return "redirect:/projets?error=notfound";
        }

        Commande commande = new Commande();
        commande.setProduit(produit);
        commande.setQuantite(1);

        // 👤 Pré-remplir avec les infos de l'utilisateur connecté
        if (userDetails != null && userDetails.getUser() != null) {
            commande.setNomClient(userDetails.getUser().getNom());
            commande.setTelephone(userDetails.getUser().getTelephone());
        }

        model.addAttribute("produit", produit);
        model.addAttribute("commande", commande);
        return "user/commande-form";
    }

    // 🛒 VALIDER LA COMMANDE
    @PostMapping("/commande/valider")
    public String validerCommande(@ModelAttribute Commande commande,
            @RequestParam Long produitId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Produit produit = produitService.getById(produitId);
        if (produit == null) {
            return "redirect:/projets?error=notfound";
        }

        commande.setProduit(produit);
        commande.setTotal(produit.getPrix()); // Simplifié pour l'instant (prix unitaire x1)

        if (commande.getQuantite() > 0) {
            commande.setTotal(produit.getPrix() * commande.getQuantite());
        }

        commande.setDateCommande(LocalDateTime.now());
        commande.setStatut("EN_ATTENTE");
        commande.setAdresse(commande.getLocalisation()); // Synchronize fields

        // 👤 Assigner l'utilisateur si connecté
        if (userDetails != null) {
            commande.setUser(userDetails.getUser());
        }

        commandeService.save(commande);

        return "redirect:/projets?success=commande";
    }
}
