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

    @org.springframework.beans.factory.annotation.Autowired
    private ecom_blog.service.CommandeTimerService timerService;

    @org.springframework.beans.factory.annotation.Autowired
    private ecom_blog.service.CreneauHoraireService creneauService;

    @org.springframework.beans.factory.annotation.Autowired
    private ecom_blog.service.NotificationService notificationService;

    @org.springframework.beans.factory.annotation.Autowired
    private ecom_blog.service.MapboxService mapboxService;

    // 🛒 VALIDER LA COMMANDE
    @PostMapping("/commande/valider")
    public String validerCommande(@ModelAttribute Commande commande,
            @RequestParam Long produitId,
            @RequestParam(required = false) Long creneauId,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng,
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

        // Par défaut, données masquées aux fournisseurs
        commande.setDonneesVisiblesFournisseur(false);

        // Sauvegarder les coordonnées GPS du client
        if (lat != null && lng != null) {
            commande.setLatitudeClient(lat);
            commande.setLongitudeClient(lng);
            commande.setLatitudeDestination(lat);
            commande.setLongitudeDestination(lng);
        } else {
            // Tenter de géocoder l'adresse
            try {
                double[] coords = mapboxService.geocodeAddress(commande.getAdresse());
                if (coords != null) {
                    commande.setLongitudeDestination(coords[0]);
                    commande.setLatitudeDestination(coords[1]);
                    commande.setLongitudeClient(coords[0]);
                    commande.setLatitudeClient(coords[1]);
                } else {
                    // Fallback Abidjan
                    commande.setLongitudeDestination(-3.9926);
                    commande.setLatitudeDestination(5.3600);
                    commande.setLongitudeClient(-3.9926);
                    commande.setLatitudeClient(5.3600);
                }
            } catch (Exception e) {
                // Fallback Abidjan
                commande.setLongitudeDestination(-3.9926);
                commande.setLatitudeDestination(5.3600);
                commande.setLongitudeClient(-3.9926);
                commande.setLatitudeClient(5.3600);
            }
        }

        // 👤 Assigner l'utilisateur si connecté
        if (userDetails != null) {
            commande.setUser(userDetails.getUser());
        }

        // Assigner le créneau si sélectionné
        if (creneauId != null) {
            creneauService.findById(creneauId).ifPresent(creneau -> {
                commande.setCreneau(creneau);
                commande.setDateCreneauDebut(creneau.getDebut());
                commande.setDateCreneauFin(creneau.getFin());
                // Réserver le créneau
                creneauService.reserverCreneau(creneau, commande);
            });
        }

        // Initialiser le timer d'expiration (10 mins)
        timerService.initialiserTimer(commande);

        commandeService.save(commande);

        // Notifier l'admin
        notificationService.notifierNouvelleCommande(commande);

        // Rediriger vers le suivi de commande
        return "redirect:/suivi-commande/" + commande.getId();
    }
}
