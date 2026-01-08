package ecom_blog.controller;

import ecom_blog.model.Commande;
import ecom_blog.service.CommandeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/commandes")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCommandeController {

    private final CommandeService commandeService;

    public AdminCommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    @org.springframework.beans.factory.annotation.Autowired
    private ecom_blog.service.CommandeTimerService timerService;

    @org.springframework.beans.factory.annotation.Autowired
    private ecom_blog.service.MasquageDonneesService masquageService;

    @org.springframework.beans.factory.annotation.Autowired
    private ecom_blog.service.NotificationService notificationService;

    // 📦 LISTE DES COMMANDES
    @GetMapping
    public String listeCommandes(Model model) {
        model.addAttribute("commandes", commandeService.getAll());
        return "admin/Commandes"; // ⚠️ respecter la casse exacte du fichier
    }

    // 🔄 MISE À JOUR DU STATUT
    @PostMapping("/update/{id}")
    public String updateStatut(@PathVariable Long id,
            @RequestParam String statut) {

        Commande commande = commandeService.getById(id);

        if (commande != null) {
            commande.setStatut(statut);

            // Si annulée, libérer le créneau
            if ("ANNULEE".equals(statut) || "EXPIREE".equals(statut)) {
                timerService.expireCommande(commande);
            }

            commandeService.save(commande);
        }

        return "redirect:/admin/commandes";
    }

    // ✅ ACCEPTER LA COMMANDE (Arrête le timer)
    @PostMapping("/accepter/{id}")
    public String accepterCommande(@PathVariable Long id) {
        Commande commande = commandeService.getById(id);

        if (commande != null && "EN_ATTENTE".equals(commande.getStatut())) {
            // Arrêter le timer (conceptuel, on met juste à jour le statut)
            timerService.annulerTimer(commande);

            // Mettre à jour le statut
            commande.setStatut("ACCEPTEE");

            // Démasquer les données
            masquageService.demasquerCommande(commande);

            commandeService.save(commande);

            // Notifier le client
            notificationService.notifierAcceptationCommande(commande);
        }

        return "redirect:/admin/commandes";
    }
}
