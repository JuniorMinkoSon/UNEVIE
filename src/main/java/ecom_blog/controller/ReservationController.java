package ecom_blog.controller;

import ecom_blog.dto.CreateReservationDto;
import ecom_blog.model.*;
import ecom_blog.service.FournisseurService;
import ecom_blog.service.ReservationService;
import ecom_blog.service.UserService;
import ecom_blog.repository.EvaluationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private FournisseurService fournisseurService;

    @Autowired
    private UserService userService;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private ecom_blog.service.CommandeService commandeService;

    @Autowired
    private ecom_blog.service.CommandeTimerService timerService;

    @Autowired
    private ecom_blog.service.NotificationService notificationService;

    @Autowired
    private ecom_blog.service.MapboxService mapboxService;

    // ... (rest of the file)

    // ==================== PAGES SECTEURS ====================

    @GetMapping("/secteurs")
    public String secteurs(Model model) {
        long countVoitures = fournisseurService.countBySecteur(Secteur.VOITURE);
        long countLoisirs = fournisseurService.countBySecteur(Secteur.LOISIRS);
        long countAlimentaire = fournisseurService.countBySecteur(Secteur.ALIMENTAIRE);
        long countEvenementiel = fournisseurService.countBySecteur(Secteur.EVENEMENTIEL);

        model.addAttribute("countVoitures", countVoitures);
        model.addAttribute("countLoisirs", countLoisirs);
        model.addAttribute("countAlimentaire", countAlimentaire);
        model.addAttribute("countEvenementiel", countEvenementiel);

        return "user/secteurs";
    }

    @GetMapping("/voitures")
    public String voitures(Model model) {
        List<Fournisseur> fournisseurs = fournisseurService.findBySecteur(Secteur.VOITURE);
        List<ServiceFournisseur> services = fournisseurService.getServicesPopulaires(Secteur.VOITURE);

        model.addAttribute("secteur", "Location de Voitures");
        model.addAttribute("secteurCode", "VOITURE");
        model.addAttribute("fournisseurs", fournisseurs);
        model.addAttribute("services", services);

        return "user/secteur-voitures";
    }

    @GetMapping("/loisirs")
    public String loisirs(Model model) {
        List<Fournisseur> fournisseurs = fournisseurService.findBySecteur(Secteur.LOISIRS);
        List<ServiceFournisseur> services = fournisseurService.getServicesPopulaires(Secteur.LOISIRS);

        model.addAttribute("secteur", "Loisirs & Activités");
        model.addAttribute("secteurCode", "LOISIRS");
        model.addAttribute("fournisseurs", fournisseurs);
        model.addAttribute("services", services);

        return "user/secteur-loisirs";
    }

    @GetMapping("/alimentaire")
    public String alimentaire(Model model) {
        List<Fournisseur> fournisseurs = fournisseurService.findBySecteur(Secteur.ALIMENTAIRE);
        List<ServiceFournisseur> services = fournisseurService.getServicesPopulaires(Secteur.ALIMENTAIRE);

        model.addAttribute("secteur", "Restaurants & Cuisine");
        model.addAttribute("secteurCode", "ALIMENTAIRE");
        model.addAttribute("fournisseurs", fournisseurs);
        model.addAttribute("services", services);

        return "user/secteur-alimentaire";
    }

    @GetMapping("/evenementiel")
    public String evenementiel(Model model) {
        List<Fournisseur> fournisseurs = fournisseurService.findBySecteur(Secteur.EVENEMENTIEL);
        List<ServiceFournisseur> services = fournisseurService.getServicesPopulaires(Secteur.EVENEMENTIEL);

        model.addAttribute("secteur", "Événementiel");
        model.addAttribute("secteurCode", "EVENEMENTIEL");
        model.addAttribute("fournisseurs", fournisseurs);
        model.addAttribute("services", services);

        return "user/secteur-evenementiel";
    }

    // ==================== DÉTAILS FOURNISSEUR ====================

    @GetMapping("/fournisseur/{id}")
    public String detailsFournisseur(@PathVariable Long id, Model model) {
        Fournisseur fournisseur = fournisseurService.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));

        List<ServiceFournisseur> services = fournisseurService.getServicesActifsByFournisseur(fournisseur);

        model.addAttribute("fournisseur", fournisseur);
        model.addAttribute("services", services);

        return "user/fournisseur-details";
    }

    // ==================== DÉTAILS SERVICE & RÉSERVATION ====================

    @GetMapping("/service/{id}")
    public String detailsService(@PathVariable Long id, Model model) {
        ServiceFournisseur service = fournisseurService.findServiceById(id)
                .orElseThrow(() -> new RuntimeException("Service non trouvé"));

        model.addAttribute("service", service);
        model.addAttribute("fournisseur", service.getFournisseur());

        List<Evaluation> evaluations = evaluationRepository.findByServiceId(id);
        model.addAttribute("evaluations", evaluations);

        CreateReservationDto dto = new CreateReservationDto();
        dto.setServiceId(id);
        model.addAttribute("reservationDto", dto);

        return "user/service-details";
    }

    @PostMapping("/reserver")
    public String reserver(@AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute("reservationDto") CreateReservationDto dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            if (dto.getServiceId() == null) {
                return "redirect:/reservation/secteurs";
            }
            ServiceFournisseur service = fournisseurService.findServiceById(dto.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Service non trouvé"));
            model.addAttribute("service", service);
            model.addAttribute("fournisseur", service.getFournisseur());
            return "user/service-details";
        }

        if (dto.getServiceId() == null) {
            return "redirect:/reservation/secteurs";
        }

        ServiceFournisseur service = fournisseurService.findServiceById(dto.getServiceId())
                .orElseThrow(() -> new RuntimeException("Service non trouvé"));

        if (service.getSecteur() == Secteur.VOITURE && !dto.isContratAccepte()) {
            model.addAttribute("service", service);
            model.addAttribute("fournisseur", service.getFournisseur());
            model.addAttribute("errorContrat", "Vous devez accepter le contrat juridique pour réserver un véhicule");
            return "user/service-details";
        }

        User user = userService.findByEmailOptional(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 🍔 Si c'est le secteur ALIMENTAIRE ou LOISIRS, on crée une COMMANDE au lieu
        // d'une
        // RÉSERVATION
        if (service.getSecteur() == Secteur.ALIMENTAIRE || service.getSecteur() == Secteur.LOISIRS) {
            Commande commande = new Commande();
            commande.setService(service);
            commande.setUser(user);
            commande.setNomClient(dto.getNomClient());
            commande.setTelephone(dto.getTelephone());

            // On utilise le nombre de personnes comme quantité
            int quantite = (dto.getNombrePersonnes() != null && dto.getNombrePersonnes() > 0)
                    ? dto.getNombrePersonnes()
                    : 1;
            commande.setQuantite(quantite);

            // Calcul du total
            double prixUnitaire = service.getPrix() != null ? service.getPrix() : 0.0;
            commande.setTotal(prixUnitaire * quantite);

            commande.setDateCommande(java.time.LocalDateTime.now());
            commande.setStatut("EN_ATTENTE");
            commande.setModePaiement("ESPECE"); // Paiement en espèce par défaut pour l'instant

            // Pour LOISIRS, définir le créneau horaire
            if (service.getSecteur() == Secteur.LOISIRS && dto.getHeureReservation() != null) {
                commande.setDateCreneauDebut(
                        java.time.LocalDateTime.of(dto.getDateService(), dto.getHeureReservation()));
            }

            // On utilise l'adresse fournie ou celle du fournisseur par défaut
            if (dto.getAdresse() != null && !dto.getAdresse().trim().isEmpty()) {
                commande.setAdresse(dto.getAdresse());
                commande.setLocalisation(dto.getAdresse()); // Ville/Localisation simplifiée
            } else {
                commande.setAdresse(service.getFournisseur().getAdresse());
                commande.setLocalisation(service.getFournisseur().getVille());
            }

            commande.setDonneesVisiblesFournisseur(false);

            // Initialiser le timer (10 mins)
            timerService.initialiserTimer(commande);

            // Tenter de géocoder l'adresse pour la map ou utiliser les coordonnées du DTO
            if (dto.getLat() != null && dto.getLng() != null) {
                commande.setLatitudeDestination(dto.getLat());
                commande.setLongitudeDestination(dto.getLng());
                commande.setLatitudeClient(dto.getLat());
                commande.setLongitudeClient(dto.getLng());
            } else {
                try {
                    // IMPORTANT: Utiliser l'adresse de la commande qu'on vient de définir
                    double[] coords = mapboxService.geocodeAddress(commande.getAdresse());
                    if (coords != null) {
                        commande.setLongitudeDestination(coords[0]);
                        commande.setLatitudeDestination(coords[1]);
                        commande.setLongitudeClient(coords[0]);
                        commande.setLatitudeClient(coords[1]);
                    } else {
                        // Fallback Abidjan (au lieu de Dakar)
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

            commandeService.save(commande);

            // Notifier
            notificationService.notifierNouvelleCommande(commande);

            return "redirect:/suivi-commande/" + commande.getId();
        }

        Reservation reservation = reservationService.creerReservation(
                user,
                dto.getServiceId(),
                dto.getNomClient(),
                dto.getTelephone(),
                dto.getEmail(),
                dto.getDateService(),
                dto.getDateFinService(),
                dto.getNombrePersonnes(),
                dto.getNombreJours(),
                dto.isAvecChauffeur(),
                dto.getNotes());

        return "redirect:/reservation/confirmation/" + reservation.getId();
    }

    @GetMapping("/confirmation/{id}")
    public String confirmation(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        model.addAttribute("reservation", reservation);

        return "user/reservation-confirmation";
    }

    // ==================== MES RÉSERVATIONS ====================

    @GetMapping("/mes-reservations")
    public String mesReservations(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByEmailOptional(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        List<Reservation> reservations = reservationService.getReservationsClient(user.getId());

        // On vérifie pour chaque réservation terminée si elle a déjà été évaluée
        reservations.forEach(r -> {
            if (r.getStatut() == StatutReservation.TERMINE) {
                boolean isEvalue = evaluationRepository.existsByReservationId(r.getId());
                // On peut utiliser une note/commentaire existant ou juste un flag transient
                // Pour simplifier ici, on va passer une Map au modèle
            }
        });

        // Version plus propre : Passer une liste d'IDs évalués
        List<Long> evaluatedIds = reservations.stream()
                .filter(r -> r.getStatut() == StatutReservation.TERMINE)
                .map(Reservation::getId)
                .filter(id -> evaluationRepository.existsByReservationId(id))
                .toList();

        model.addAttribute("reservations", reservations);
        model.addAttribute("evaluatedIds", evaluatedIds);

        return "user/mes-reservations";
    }

    @GetMapping("/details/{id}")
    public String detailsReservation(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        model.addAttribute("reservation", reservation);

        return "user/reservation-details";
    }

    // ==================== RECHERCHE ====================

    @GetMapping("/recherche")
    public String recherche(@RequestParam(required = false) String q,
            @RequestParam(required = false) Secteur secteur,
            Model model) {

        List<ServiceFournisseur> services;
        List<Fournisseur> fournisseurs;

        if (q != null && !q.isEmpty()) {
            services = fournisseurService.rechercherServices(q);
            fournisseurs = fournisseurService.rechercher(q);
        } else if (secteur != null) {
            services = fournisseurService.getServicesBySecteur(secteur);
            fournisseurs = fournisseurService.findBySecteur(secteur);
        } else {
            services = List.of();
            fournisseurs = fournisseurService.findActifs();
        }

        model.addAttribute("query", q);
        model.addAttribute("secteur", secteur);
        model.addAttribute("services", services);
        model.addAttribute("fournisseurs", fournisseurs);

        return "user/recherche";
    }
}
