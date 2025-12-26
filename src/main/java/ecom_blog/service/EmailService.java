package ecom_blog.service;

import ecom_blog.model.Commande;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.email.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Envoie un email de confirmation de commande créée
     */
    public void envoyerEmailCommandeCreee(Commande commande) {
        try {
            Context context = new Context();
            context.setVariable("commande", commande);
            context.setVariable("client", commande.getNomClient());

            String content = templateEngine.process("email/commande-creee", context);

            envoyerEmail(
                    commande.getUser() != null ? commande.getUser().getEmail() : null,
                    "Confirmation de votre commande #" + commande.getId(),
                    content);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email de commande créée: " + e.getMessage());
        }
    }

    /**
     * Envoie un email quand un prestataire est assigné
     */
    public void envoyerEmailPrestataireAssigne(Commande commande) {
        try {
            Context context = new Context();
            context.setVariable("commande", commande);
            context.setVariable("prestataire", commande.getPrestataire());
            context.setVariable("lienSuivi", "http://localhost:8082/commande/suivi/" + commande.getId());

            String content = templateEngine.process("email/prestataire-assigne", context);

            envoyerEmail(
                    commande.getUser() != null ? commande.getUser().getEmail() : null,
                    "Votre prestataire est en route !",
                    content);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email prestataire assigné: " + e.getMessage());
        }
    }

    /**
     * Envoie un email quand le prestataire est en route
     */
    public void envoyerEmailPrestataireEnRoute(Commande commande) {
        try {
            Context context = new Context();
            context.setVariable("commande", commande);
            context.setVariable("prestataire", commande.getPrestataire());
            context.setVariable("lienSuivi", "http://localhost:8082/commande/suivi/" + commande.getId());

            String content = templateEngine.process("email/prestataire-en-route", context);

            envoyerEmail(
                    commande.getUser() != null ? commande.getUser().getEmail() : null,
                    "Votre prestataire arrive bientôt !",
                    content);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email prestataire en route: " + e.getMessage());
        }
    }

    /**
     * Envoie un email de confirmation de livraison
     */
    public void envoyerEmailCommandeLivree(Commande commande) {
        try {
            Context context = new Context();
            context.setVariable("commande", commande);

            String content = templateEngine.process("email/commande-livree", context);

            envoyerEmail(
                    commande.getUser() != null ? commande.getUser().getEmail() : null,
                    "Votre commande a été livrée !",
                    content);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email de livraison: " + e.getMessage());
        }
    }

    /**
     * Méthode privée pour envoyer un email
     */
    private void envoyerEmail(String to, String subject, String htmlContent) throws MessagingException {
        if (to == null || to.isEmpty()) {
            System.out.println("Aucun email destinataire spécifié");
            return;
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
        System.out.println("Email envoyé à " + to + ": " + subject);
    }
}
