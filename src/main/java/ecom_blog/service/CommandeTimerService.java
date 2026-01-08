package ecom_blog.service;

import ecom_blog.model.Commande;
import ecom_blog.model.StatutCommande;
import ecom_blog.repository.CommandeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CommandeTimerService - Gère l'expiration automatique des commandes
 * Vérifie toutes les minutes les commandes non acceptées dans les 10 minutes
 */
@Service
public class CommandeTimerService {

    private static final Logger logger = LoggerFactory.getLogger(CommandeTimerService.class);

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired(required = false)
    private CreneauHoraireService creneauService;

    @Value("${commande.timer.expiration.minutes:5}")
    private int expirationMinutes;

    /**
     * Vérifie toutes les minutes les commandes expirées
     * Tâche planifiée qui s'exécute automatiquement
     */
    @Scheduled(fixedRate = 60000) // Toutes les 60 secondes
    @Transactional
    public void verifierCommandesExpirees() {
        LocalDateTime maintenant = LocalDateTime.now();

        // Récupérer les commandes en attente non expirées
        List<Commande> commandesEnAttente = commandeRepository
                .findByStatutAndExpireeFalse(StatutCommande.EN_ATTENTE.name());

        logger.debug("Vérification de {} commandes en attente", commandesEnAttente.size());

        for (Commande commande : commandesEnAttente) {
            if (commande.getDateExpiration() != null &&
                    commande.getDateExpiration().isBefore(maintenant)) {

                logger.info("Commande {} expirée - Annulation", commande.getId());
                expireCommande(commande);
            }
        }
    }

    /**
     * Expire une commande et tente de la rediriger
     */
    @Transactional
    public void expireCommande(Commande commande) {
        commande.setExpiree(true);
        commande.setStatut(StatutCommande.EXPIREE.name());

        // Libérer le créneau si réservé
        if (commande.getCreneau() != null && creneauService != null) {
            creneauService.libererCreneau(commande.getCreneau());
        }

        // Tenter redirection vers autre entreprise
        boolean redirigee = redirigerVersAutreEntreprise(commande);

        if (redirigee) {
            commande.setRedirigee(true);
            commande.setStatut(StatutCommande.REDIRIGEE.name());
            logger.info("Commande {} redirigée vers autre entreprise", commande.getId());

            // Notifier client de la redirection
            notificationService.notifierRedirection(commande);
        } else {
            // Aucune autre entreprise disponible - annulation
            commande.setStatut(StatutCommande.ANNULEE.name());
            logger.warn("Commande {} annulée - Aucune entreprise disponible", commande.getId());

            // Notifier client de l'annulation
            notificationService.notifierAnnulation(commande);
        }

        commandeRepository.save(commande);
    }

    /**
     * Tente de rediriger une commande vers une autre entreprise
     * TODO: Implémenter la logique de recherche d'entreprises alternatives
     */
    private boolean redirigerVersAutreEntreprise(Commande commande) {
        // Pour l'instant, retourne false
        // À implémenter selon la logique métier:
        // 1. Trouver autres fournisseurs du même secteur
        // 2. Vérifier leur disponibilité
        // 3. Créer nouvelle commande avec nouveau timer
        // 4. Notifier nouveau fournisseur

        logger.debug("Recherche d'entreprise alternative pour commande {}", commande.getId());

        // TODO: Implémenter la logique de redirection
        // Exemple:
        // - Chercher fournisseurs avec même produit/service
        // - Vérifier créneaux disponibles
        // - Créer nouvelle commande
        // - Retourner true si succès

        return false; // Pas de redirection pour l'instant
    }

    /**
     * Initialise le timer d'une nouvelle commande
     */
    @Transactional
    public void initialiserTimer(Commande commande) {
        LocalDateTime maintenant = LocalDateTime.now();
        commande.setDateCreation(maintenant);
        commande.setDateExpiration(maintenant.plusMinutes(expirationMinutes));
        commande.setExpiree(false);
        commande.setRedirigee(false);

        logger.info("Timer initialisé pour commande {} - Expiration à {}",
                commande.getId(), commande.getDateExpiration());
    }

    /**
     * Annule le timer (quand commande acceptée)
     */
    @Transactional
    public void annulerTimer(Commande commande) {
        // Le timer reste mais on ne vérifie plus les commandes acceptées
        logger.info("Timer annulé pour commande {} - Commande acceptée", commande.getId());
    }
}
