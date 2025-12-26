package ecom_blog.controller;

import ecom_blog.model.Commande;
import ecom_blog.model.Produit;
import ecom_blog.model.enums.StatutCommande;
import ecom_blog.security.CustomUserDetails;
import ecom_blog.service.CommandeService;
import ecom_blog.service.EmailService;
import ecom_blog.service.ProduitService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CommandeController {

    private final ProduitService produitService;
    private final CommandeService commandeService;
    private final EmailService emailService;

    public CommandeController(ProduitService produitService,
            CommandeService commandeService,
            EmailService emailService) {
        this.produitService = produitService;
        this.commandeService = commandeService;
        this.emailService = emailService;
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

        // Remplissage des champs obligatoires par défaut (à améliorer avec un vrai
        // formulaire)
        commande.setAdresse("Adresse par défaut");
        commande.setTelephone("0000000000");
        commande.setNomClient("Client");

        // 👤 Si utilisateur connecté, on pré-remplit avec ses infos
        if (userDetails != null) {
            commande.setUser(userDetails.getUser());
            commande.setNomClient(userDetails.getUser().getNom());
            commande.setTelephone(userDetails.getUser().getTelephone());
        }

        commandeService.save(commande);

        // Initialiser le statut détaillé
        commande.setStatutDetaille(StatutCommande.CREEE);
        commandeService.save(commande);

        // Envoyer email de confirmation
        emailService.envoyerEmailCommandeCreee(commande);

        return "redirect:/projets?success=commande";
    }

    // 📍 PAGE DE SUIVI DE COMMANDE
    @GetMapping("/commande/suivi/{id}")
    public String afficherSuiviCommande(@PathVariable Long id, Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Commande commande = commandeService.getById(id);

        // Vérifier que l'utilisateur a le droit de voir cette commande
        if (userDetails != null && commande.getUser() != null
                && !commande.getUser().getId().equals(userDetails.getUser().getId())) {
            return "redirect:/mes-commandes?error=access";
        }

        model.addAttribute("commande", commande);
        return "user/suivi-commande";
    }
}
