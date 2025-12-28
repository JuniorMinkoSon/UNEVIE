package ecom_blog.service;

import ecom_blog.model.Entreprise;
import ecom_blog.model.Quartier;
import ecom_blog.model.Article;
import ecom_blog.model.Categorie;
import ecom_blog.model.Produit;
import ecom_blog.model.enums.CategorieEntreprise;
import ecom_blog.repository.EntrepriseRepository;
import ecom_blog.repository.QuartierRepository;
import ecom_blog.repository.ArticleRepository;
import ecom_blog.repository.CategorieRepository;
import ecom_blog.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializerService implements CommandLineRunner {

        private final QuartierRepository quartierRepository;
        private final EntrepriseRepository entrepriseRepository;
        private final ProduitRepository produitRepository;
        private final ArticleRepository articleRepository;
        private final CategorieRepository categorieRepository;

        @Override
        public void run(String... args) throws Exception {
                // Vérifier si des données existent déjà
                // NOTE: Décommentez ces lignes pour éviter de recréer les données à chaque
                // démarrage
                /*
                 * if (quartierRepository.count() > 0) {
                 * log.info("Les données de démonstration existent déjà");
                 * return;
                 * }
                 */

                log.info(" Initialisation des données de démonstration UNEVIE...");

                // Créer les quartiers
                Quartier cocody = createQuartier("Cocody", "Abidjan", 5.3599, -3.9866, 5.0);
                Quartier yopougon = createQuartier("Yopougon", "Abidjan", 5.3433, -4.0587, 5.0);
                Quartier plateau = createQuartier("Plateau", "Abidjan", 5.3267, -4.0122, 3.0);
                Quartier marcory = createQuartier("Marcory", "Abidjan", 5.2964, -3.9777, 4.0);
                Quartier treichville = createQuartier("Treichville", "Abidjan", 5.2928, -4.0059, 3.5);

                log.info(" Quartiers créés");

                // Créer des entreprises de véhicules
                createEntreprise(
                                "Babi Location",
                                "Location de véhicules haut de gamme avec chauffeur disponible. Flotte moderne et bien entretenue.",
                                CategorieEntreprise.VEHICULE,
                                cocody,
                                "+225 07 00 00 01",
                                "contact@babilocation.ci",
                                LocalDate.of(2024, 1, 1),
                                LocalDate.of(2026, 12, 31));

                createEntreprise(
                                "Sponguy Location",
                                "Spécialiste de la location de véhicules pour tous vos déplacements. Service 24/7.",
                                CategorieEntreprise.VEHICULE,
                                yopougon,
                                "+225 07 00 00 02",
                                "info@sponguylocation.ci",
                                LocalDate.of(2024, 3, 1),
                                LocalDate.of(2026, 2, 28));

                // Créer des entreprises de résidences
                createEntreprise(
                                "Résidences Premium CI",
                                "Locations de résidences meublées standing pour séjours courts et longs. Confort garanti.",
                                CategorieEntreprise.RESIDENCE,
                                plateau,
                                "+225 07 00 00 03",
                                "contact@residencespremium.ci",
                                LocalDate.of(2024, 2, 1),
                                LocalDate.of(2025, 1, 31));

                createEntreprise(
                                "Assinie Beach Resort",
                                "Villas et appartements en bord de mer à Assinie. Cadre idyllique pour vos vacances.",
                                CategorieEntreprise.RESIDENCE,
                                cocody,
                                "+225 07 00 00 04",
                                "booking@assinieresort.ci",
                                LocalDate.of(2024, 6, 1),
                                LocalDate.of(2026, 5, 31));

                // Créer des traiteurs
                createEntreprise(
                                "Délices d'Afrique",
                                "Traiteur spécialisé dans la cuisine africaine et internationale. Pour tous vos événements.",
                                CategorieEntreprise.TRAITEUR,
                                marcory,
                                "+225 07 00 00 05",
                                "commandes@delicesdafrique.ci",
                                LocalDate.of(2023, 10, 1),
                                LocalDate.of(2025, 9, 30));

                createEntreprise(
                                "Royal Catering Services",
                                "Service traiteur haut de gamme pour mariages, séminaires et événements corporate.",
                                CategorieEntreprise.TRAITEUR,
                                treichville,
                                "+225 07 00 00 06",
                                "events@royalcatering.ci",
                                LocalDate.of(2024, 1, 15),
                                LocalDate.of(2026, 1, 14));

                // Créer une entreprise événementielle
                createEntreprise(
                                "EventMakers Pro",
                                "Organisation complète d'événements : mariages, anniversaires, séminaires. Équipe professionnelle.",
                                CategorieEntreprise.EVENEMENT,
                                cocody,
                                "+225 07 00 00 07",
                                "hello@eventmakers.ci",
                                LocalDate.of(2024, 4, 1),
                                LocalDate.of(2027, 3, 31));

                log.info("Entreprises créées");

                // Créer des catégories de blog
                Categorie techCategory = createCategorie("Technologie",
                                "Articles sur les nouvelles technologies et innovations");
                Categorie businessCategory = createCategorie("Business", "Conseils et actualités business");
                Categorie lifestyleCategory = createCategorie("Lifestyle", "Mode de vie, voyages et culture");
                Categorie immobilierCategory = createCategorie("Immobilier", "Actualités et tendances de l'immobilier");

                log.info("Catégories blog créées");

                // Créer des articles
                createArticle(
                                "L'essor de la location de véhicules en Côte d'Ivoire",
                                "location-vehicules-ci",
                                "Découvrez comment le marché de la location de véhicules se transforme en Côte d'Ivoire. Avec l'émergence de plateformes comme UNEVIE, louer une voiture n'a jamais été aussi simple.",
                                techCategory);

                createArticle(
                                "Les meilleurs quartiers pour investir à Abidjan",
                                "quartiers-investir-abidjan",
                                "Abidjan offre de nombreuses opportunités d'investissement immobilier. Cocody reste le quartier privilégié pour les résidences haut de gamme.",
                                immobilierCategory);

                createArticle(
                                "Comment organiser un événement réussi en 2025",
                                "organiser-evenement-2025",
                                "L'organisation d'événements a évolué avec les nouvelles technologies. Découvrez comment créer des expériences uniques pour vos invités.",
                                lifestyleCategory);

                createArticle(
                                "Les avantages du modèle EasyService pour les entreprises",
                                "modele-easyservice-entreprises",
                                "EasyService révolutionne la façon dont les entreprises partenaires gèrent leurs services. Découvrez les avantages de ce modèle d'intermédiation.",
                                businessCategory);

                log.info("Articles blog créés");
                log.info(" Initialisation terminée ! /entreprises et /blog disponibles");
        }

        private Quartier createQuartier(String nom, String commune, Double lat, Double lng, Double rayon) {
                Quartier quartier = new Quartier();
                quartier.setNom(nom);
                quartier.setCommune(commune);
                quartier.setLatitude(lat);
                quartier.setLongitude(lng);
                quartier.setRayonCouverture(rayon);
                return quartierRepository.save(quartier);
        }

        private Entreprise createEntreprise(String nom, String description, CategorieEntreprise categorie,
                        Quartier quartier, String telephone, String email,
                        LocalDate dateDebut, LocalDate dateFin) {
                Entreprise entreprise = new Entreprise();
                entreprise.setNom(nom);
                entreprise.setDescription(description);
                entreprise.setCategorie(categorie);
                entreprise.setQuartier(quartier);
                entreprise.setTelephone(telephone);
                entreprise.setEmail(email);
                entreprise.setContratActif(true);
                entreprise.setDateDebutContrat(dateDebut);
                entreprise.setDateFinContrat(dateFin);
                entreprise.setCommissionPourcentage(10.0);
                entreprise.setLatitude(quartier.getLatitude());
                entreprise.setLongitude(quartier.getLongitude());
                return entrepriseRepository.save(entreprise);
        }

        private Categorie createCategorie(String libelle, String description) {
                Categorie categorie = new Categorie();
                categorie.setLibelle(libelle);
                categorie.setDescription(description);
                return categorieRepository.save(categorie);
        }

        private Article createArticle(String titre, String slug, String contenu, Categorie categorie) {
                Article article = new Article();
                article.setTitre(titre);
                article.setSlug(slug);
                article.setContenu(contenu);
                article.setCategory(categorie);
                return articleRepository.save(article);
        }
}
