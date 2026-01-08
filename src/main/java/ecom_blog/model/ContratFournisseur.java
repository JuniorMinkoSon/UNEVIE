package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * ContratFournisseur - Contrat juridique automatisé pour chaque fournisseur
 * Précise que UneVie n'est pas responsable des litiges entre client et
 * fournisseur
 */
@Entity
@Table(name = "contrat_fournisseur")
@Getter
@Setter
public class ContratFournisseur extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Fournisseur fournisseur;

    @Column(length = 10000)
    private String contenuContrat;

    private LocalDateTime dateGeneration;
    private LocalDateTime dateSignature;

    private boolean signe = false;

    // Version du contrat (pour les mises à jour)
    private String versionContrat = "1.0";

    /**
     * Génère le contenu par défaut du contrat
     */
    public void genererContenu() {
        this.dateGeneration = LocalDateTime.now();
        this.contenuContrat = """
                CONTRAT DE PARTENARIAT UNEVIE RÉSERVATION
                ==========================================

                Entre :
                - UneVie Réservation (ci-après "la Plateforme")
                - %s (ci-après "le Fournisseur")

                Article 1 - Objet du contrat
                ----------------------------
                Le présent contrat définit les conditions de partenariat entre
                la Plateforme et le Fournisseur pour la mise en visibilité et
                la gestion des réservations de services.

                Article 2 - Responsabilités de la Plateforme
                --------------------------------------------
                La Plateforme s'engage à :
                - Assurer la visibilité des services du Fournisseur
                - Gérer le système de réservation en ligne
                - Fournir un suivi des réservations et des paiements
                - Prélever une commission de 5%% sur chaque réservation acceptée

                Article 3 - Limitation de responsabilité
                ----------------------------------------
                ARTICLE 3: LIMITATION DE RESPONSABILITÉ

                La Plateforme agit en qualité d’intermédiaire technique de mise en relation entre les Clients et le Fournisseur.

                À ce titre, la Plateforme ne saurait être tenue responsable de l’exécution, de la qualité ou des conséquences des services fournis par le Fournisseur.

                Toutefois, la Plateforme demeure responsable des obligations lui incombant au titre du présent contrat, notamment en ce qui concerne le fonctionnement du système de réservation, la gestion des paiements et la transmission des informations.

                Le Fournisseur demeure seul responsable de ses prestations et des relations contractuelles qu’il entretient avec les Clients.

                Article 4 - Commission
                ----------------------
                La Plateforme prélève une commission de 5%% sur le montant total
                de chaque réservation acceptée par le Fournisseur.

                Article 5 - Durée et résiliation
                --------------------------------
                Ce contrat est conclu pour une durée indéterminée. Chaque partie
                peut le résilier avec un préavis de 30 jours.

                Fait à Abidjan, Côte d'Ivoire
                Date de génération : %s
                """
                .formatted(
                        fournisseur.getNomEntreprise(),
                        dateGeneration.toString());
    }

    /**
     * Marque le contrat comme signé
     */
    public void signer() {
        this.signe = true;
        this.dateSignature = LocalDateTime.now();
    }
}
