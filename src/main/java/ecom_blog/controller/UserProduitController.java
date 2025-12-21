package ecom_blog.controller;

import ecom_blog.model.Commande;
import ecom_blog.model.Produit;
import ecom_blog.model.User;
import ecom_blog.service.ArticleService;
import ecom_blog.service.CommandeService;
import ecom_blog.service.ProduitService;
import ecom_blog.service.UserService;
import ecom_blog.service.WaveService;
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

    @Autowired
    private WaveService waveService;

    /**
     * Page projets / produits avec filtre par catégorie
     */
    @GetMapping("/projets")
    public String projets(
            @RequestParam(required = false) String categorie,
            Model model) {
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
     * Etape 1 : Afficher le formulaire de commande
     */
    @GetMapping("/produits/commander/{id}")
    public String afficherFormulaireCommande(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Produit produit = produitService.getById(id);
        if (produit == null || !produit.isDisponible()) {
            return "redirect:/projets?error=notfound";
        }

        model.addAttribute("produit", produit);
        model.addAttribute("commande", new Commande());
        return "user/commande-form";
    }

    /**
     * Etape 2 : Valider la commande
     */
    @PostMapping("/produits/valider-commande")
    public String validerCommande(
            @ModelAttribute Commande commande,
            @RequestParam Long produitId,
            Principal principal) {
        Produit produit = produitService.getById(produitId);
        if (produit == null)
            return "redirect:/projets";

        // Associer le produit
        commande.setProduit(produit);

        // Utilisateur (optionnel si non connecté)
        if (principal != null) {
            User user = userService.findByEmail(principal.getName());
            commande.setUser(user);
        }

        // Calcul du total
        double total = produit.getPrix();
        if (commande.getQuantite() != null && commande.getQuantite() > 1) {
            total = total * commande.getQuantite();
        }
        if (commande.getNombreJours() != null && commande.getNombreJours() > 1) {
            total = total * commande.getNombreJours();
        }
        commande.setTotal(total);

        // Date et Statut
        commande.setDateCommande(LocalDateTime.now());
        commande.setStatut("EN_ATTENTE");

        // Si l'adresse est vide mais localisation remplie, on copie
        if ((commande.getAdresse() == null || commande.getAdresse().isBlank())
                && commande.getLocalisation() != null) {
            commande.setAdresse(commande.getLocalisation());
        }

        commandeService.save(commande);

        // Si paiement par Wave, redirection vers leur portail
        if ("TELEPHONE".equals(commande.getModePaiement()) && "Wave".equals(commande.getOperateur())) {
            String checkoutUrl = waveService.createCheckoutSession(commande.getTotal(), commande.getId());
            if (checkoutUrl != null) {
                return "redirect:" + checkoutUrl;
            } else {
                return "redirect:/projets?error=wave_api_error";
            }
        }

        return "redirect:/projets?success=commande";
    }

    /**
     * Etape 3 : Afficher les commandes de l'utilisateur
     */
    @GetMapping("/mes-commandes")
    public String mesCommandes(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(principal.getName());
        List<Commande> commandes = commandeService.getByUser(user);

        model.addAttribute("commandes", commandes);
        return "user/mes-commandes";
    }
}
