package ecom_blog.controller;

import ecom_blog.model.Prestataire;
import ecom_blog.model.enums.TypePrestataire;
import ecom_blog.repository.PrestataireRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/prestataires")
public class AdminPrestataireController {

    private final PrestataireRepository prestataireRepository;

    public AdminPrestataireController(PrestataireRepository prestataireRepository) {
        this.prestataireRepository = prestataireRepository;
    }

    /**
     * Liste des prestataires
     */
    @GetMapping
    public String liste(Model model, @RequestParam(required = false) String type) {
        List<Prestataire> prestataires;

        if (type != null && !type.isEmpty()) {
            TypePrestataire typePrestataire = TypePrestataire.valueOf(type);
            prestataires = prestataireRepository.findByTypePrestataire(typePrestataire);
        } else {
            prestataires = prestataireRepository.findAll();
        }

        model.addAttribute("prestataires", prestataires);
        model.addAttribute("types", TypePrestataire.values());
        model.addAttribute("typeSelectionne", type);

        return "admin/prestataires";
    }

    /**
     * Formulaire d'ajout
     */
    @GetMapping("/ajouter")
    public String formulaireAjout(Model model) {
        model.addAttribute("prestataire", new Prestataire());
        model.addAttribute("types", TypePrestataire.values());
        return "admin/prestataire-form";
    }

    /**
     * Formulaire de modification
     */
    @GetMapping("/modifier/{id}")
    public String formulaireModification(@PathVariable Long id, Model model) {
        Prestataire prestataire = prestataireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestataire introuvable"));

        model.addAttribute("prestataire", prestataire);
        model.addAttribute("types", TypePrestataire.values());
        return "admin/prestataire-form";
    }

    /**
     * Sauvegarde (création ou modification)
     */
    @PostMapping("/sauvegarder")
    public String sauvegarder(@ModelAttribute Prestataire prestataire, RedirectAttributes redirectAttributes) {
        prestataireRepository.save(prestataire);
        redirectAttributes.addFlashAttribute("success", "Prestataire sauvegardé avec succès");
        return "redirect:/admin/prestataires";
    }

    /**
     * Suppression
     */
    @PostMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        prestataireRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Prestataire supprimé avec succès");
        return "redirect:/admin/prestataires";
    }

    /**
     * Toggle disponibilité
     */
    @PostMapping("/toggle-disponibilite/{id}")
    public String toggleDisponibilite(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Prestataire prestataire = prestataireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prestataire introuvable"));

        prestataire.setDisponible(!prestataire.getDisponible());
        prestataireRepository.save(prestataire);

        redirectAttributes.addFlashAttribute("success", "Disponibilité mise à jour");
        return "redirect:/admin/prestataires";
    }
}
