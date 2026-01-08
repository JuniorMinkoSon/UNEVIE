package ecom_blog.service;

import ecom_blog.dto.CommandeMasqueeDto;
import ecom_blog.model.Commande;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * MasquageDonneesService - Service pour masquer/démasquer les données sensibles
 * Protège les informations client jusqu'à l'acceptation de la commande
 */
@Service
public class MasquageDonneesService {

    /**
     * Masque un numéro de téléphone
     * Ex: "771234567" -> "XXX-XXX-567"
     */
    public String masquerTelephone(String telephone) {
        if (telephone == null || telephone.length() < 4) {
            return "XXX-XXX-XXXX";
        }

        String derniers = telephone.substring(telephone.length() - 3);
        return "XXX-XXX-" + derniers;
    }

    /**
     * Masque un nom complet
     * Ex: "Jean Dupont" -> "J. D."
     */
    public String masquerNom(String nom) {
        if (nom == null || nom.isEmpty()) {
            return "XX";
        }

        String[] parts = nom.split(" ");
        StringBuilder masked = new StringBuilder();

        for (String part : parts) {
            if (!part.isEmpty()) {
                masked.append(part.charAt(0)).append(". ");
            }
        }

        return masked.toString().trim();
    }

    /**
     * Masque une adresse (affiche seulement le quartier/ville)
     * Ex: "15 Rue de la Paix, Plateau, Dakar" -> "Plateau, Dakar"
     */
    public String masquerAdresse(String adresse) {
        if (adresse == null || adresse.isEmpty()) {
            return "Quartier non spécifié";
        }

        // Prendre les 2 dernières parties séparées par virgule
        String[] parts = adresse.split(",");

        if (parts.length >= 2) {
            return parts[parts.length - 2].trim() + ", " + parts[parts.length - 1].trim();
        } else if (parts.length == 1) {
            return parts[0].trim();
        }

        return "Quartier non spécifié";
    }

    /**
     * Convertit une commande en DTO avec données masquées
     */
    public CommandeMasqueeDto masquerCommande(Commande commande) {
        CommandeMasqueeDto dto = new CommandeMasqueeDto();

        dto.setId(commande.getId());
        dto.setTotal(commande.getTotal());
        dto.setDateCommande(commande.getDateCommande());
        dto.setDateCreneauDebut(commande.getDateCreneauDebut());
        dto.setDateCreneauFin(commande.getDateCreneauFin());
        dto.setStatut(commande.getStatut());
        dto.setQuantite(commande.getQuantite());

        if (commande.getProduit() != null) {
            dto.setProduitNom(commande.getProduit().getNom());
        }

        // Calculer temps restant avant expiration
        if (commande.getDateExpiration() != null) {
            Duration duration = Duration.between(LocalDateTime.now(), commande.getDateExpiration());
            dto.setTempsRestant(Math.max(0, duration.getSeconds()));
        }

        // Masquer ou non selon le statut
        if (commande.isDonneesVisiblesFournisseur()) {
            // Données démasquées
            dto.setNomClient(commande.getNomClient());
            dto.setTelephone(commande.getTelephone());
            dto.setAdresse(commande.getAdresse());
            dto.setDonneesVisibles(true);
        } else {
            // Données masquées
            dto.setNomClient(masquerNom(commande.getNomClient()));
            dto.setTelephone(masquerTelephone(commande.getTelephone()));
            dto.setAdresse(masquerAdresse(commande.getAdresse()));
            dto.setDonneesVisibles(false);
        }

        return dto;
    }

    /**
     * Démasque les données d'une commande (après acceptation)
     */
    public void demasquerCommande(Commande commande) {
        commande.setDonneesVisiblesFournisseur(true);
    }
}
