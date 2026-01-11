package ecom_blog.controller;

import ecom_blog.model.Commande;
import ecom_blog.model.CommandeItem;
import ecom_blog.model.Produit;
import ecom_blog.model.User;
import ecom_blog.service.CommandeService;
import ecom_blog.service.ProduitService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/panier")
public class CommandeController {

    private final ProduitService produitService;
    private final CommandeService commandeService;

    /** AJOUTER AU PANIER **/
    @PostMapping("/ajouter/{id}")
    public String ajouterPanier(@PathVariable Long id, HttpSession session) {

        List<Long> panier = (List<Long>) session.getAttribute("panier");
        if (panier == null) panier = new ArrayList<>();

        panier.add(id);
        session.setAttribute("panier", panier);

        return "redirect:/panier?added=true";
    }

    /** AFFICHER LE PANIER **/
    @GetMapping
    public String afficherPanier(Model model, HttpSession session) {

        List<Long> panierIds = (List<Long>) session.getAttribute("panier");

        List<Produit> produits = new ArrayList<>();
        double total = 0;

        if (panierIds != null) {
            for (Long id : panierIds) {
                Produit p = produitService.findById(id);
                if (p != null) {
                    produits.add(p);
                    total += p.getPrix();
                }
            }
        }

        model.addAttribute("panier", produits);
        model.addAttribute("total", total);

        return "user/panier";
    }

    /** VALIDER LA COMMANDE **/
    @PostMapping("/valider")
    public String validerCommande(HttpSession session,
                                  @RequestParam String modePaiement) {

        List<Long> panierIds = (List<Long>) session.getAttribute("panier");

        if (panierIds == null || panierIds.isEmpty())
            return "redirect:/panier?error=vide";

        User user = (User) session.getAttribute("user");

        Commande commande = new Commande();
        commande.setUser(user);
        commande.setModePaiement(modePaiement);
        commande.setStatut("EN_ATTENTE");

        double total = 0;

        List<CommandeItem> items = new ArrayList<>();

        for (Long id : panierIds) {

            Produit p = produitService.findById(id);

            if (p != null) {
                total += p.getPrix();

                CommandeItem item = new CommandeItem();
                item.setCommande(commande);
                item.setProduit(p);
                item.setPrix(p.getPrix());
                item.setQuantite(1);

                items.add(item);
            }
        }

        commande.setTotal(total);
        commande.setItems(items);

        commandeService.save(commande);

        session.removeAttribute("panier");

        return "redirect:/confirmation?success=true";
    }
}
