package ecom_blog.service;

import ecom_blog.model.Commande;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * NotificationService - Gère les notifications temps réel et emails
 */
@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired(required = false)
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Notifie une nouvelle commande à l'admin
     */
    public void notifierNouvelleCommande(Commande commande) {
        logger.info("Notification nouvelle commande {} à l'admin", commande.getId());

        // WebSocket notification
        if (messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/admin/commandes", commande.getId());
        }

        // TODO: Envoyer email à l'admin
    }

    /**
     * Notifie le client d'une redirection
     */
    public void notifierRedirection(Commande commande) {
        logger.info("Notification redirection commande {} au client", commande.getId());

        // WebSocket notification
        if (messagingTemplate != null && commande.getUser() != null) {
            messagingTemplate.convertAndSendToUser(
                    commande.getUser().getEmail(),
                    "/queue/notifications",
                    "Votre commande a été redirigée vers une autre entreprise");
        }

        // TODO: Envoyer email au client
    }

    /**
     * Notifie le client d'une annulation
     */
    public void notifierAnnulation(Commande commande) {
        logger.info("Notification annulation commande {} au client", commande.getId());

        // WebSocket notification
        if (messagingTemplate != null && commande.getUser() != null) {
            messagingTemplate.convertAndSendToUser(
                    commande.getUser().getEmail(),
                    "/queue/notifications",
                    "Votre commande a été annulée - Aucun fournisseur disponible");
        }

        // TODO: Envoyer email au client
    }

    /**
     * Envoie une demande d'évaluation au client
     */
    public void envoyerDemandeEvaluation(Commande commande) {
        logger.info("Envoi demande évaluation pour commande {} au client", commande.getId());

        // WebSocket notification
        if (messagingTemplate != null && commande.getUser() != null) {
            messagingTemplate.convertAndSendToUser(
                    commande.getUser().getEmail(),
                    "/queue/notifications",
                    "Votre commande est terminée ! Donnez votre avis");
        }

        // TODO: Envoyer email au client avec lien d'évaluation
    }

    /**
     * Notifie l'acceptation d'une commande
     */
    public void notifierAcceptationCommande(Commande commande) {
        logger.info("Notification acceptation commande {} au client", commande.getId());

        // WebSocket notification
        if (messagingTemplate != null && commande.getUser() != null) {
            messagingTemplate.convertAndSendToUser(
                    commande.getUser().getEmail(),
                    "/queue/notifications",
                    "Votre commande a été acceptée !");
        }

        // TODO: Envoyer email au client
    }

    /**
     * Notifie le début de livraison
     */
    public void notifierDebutLivraison(Commande commande) {
        logger.info("Notification début livraison commande {} au client", commande.getId());

        // WebSocket notification
        if (messagingTemplate != null && commande.getUser() != null) {
            messagingTemplate.convertAndSendToUser(
                    commande.getUser().getEmail(),
                    "/queue/notifications",
                    "Votre livraison a commencé ! Suivez votre livreur en temps réel");
        }

        // TODO: Envoyer email/SMS au client
    }
}
