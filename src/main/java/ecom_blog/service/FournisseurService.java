package ecom_blog.service;

import ecom_blog.model.*;
import ecom_blog.repository.ContratFournisseurRepository;
import ecom_blog.repository.FournisseurRepository;
import ecom_blog.repository.ServiceFournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FournisseurService {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    private ServiceFournisseurRepository serviceFournisseurRepository;

    @Autowired
    private ContratFournisseurRepository contratRepository;

    @Autowired
    private SearchService searchService;

    // ==================== CRUD FOURNISSEUR ====================

    public Fournisseur save(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    public Optional<Fournisseur> findById(Long id) {
        return fournisseurRepository.findById(id);
    }

    public Optional<Fournisseur> findByUserId(Long userId) {
        return fournisseurRepository.findByUserId(userId);
    }

    public List<Fournisseur> findAll() {
        return fournisseurRepository.findAll();
    }

    public List<Fournisseur> findActifs() {
        return fournisseurRepository.findByActifTrue();
    }

    public void delete(Long id) {
        fournisseurRepository.deleteById(id);
    }

    // ==================== PAR SECTEUR ====================

    public List<Fournisseur> findBySecteur(Secteur secteur) {
        return fournisseurRepository.findBySecteurAndActifTrue(secteur);
    }

    public List<Fournisseur> findPopularBySecteur(Secteur secteur) {
        return fournisseurRepository.findPopularBySecteur(secteur);
    }

    // ==================== INSCRIPTION FOURNISSEUR ====================

    /**
     * Inscrit un nouveau fournisseur avec génération automatique du contrat
     */
    public Fournisseur inscrireFournisseur(User user, String nomEntreprise, Secteur secteur,
            String description, String adresse, String ville, String telephone) {
        // Créer le fournisseur
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setUser(user);
        fournisseur.setNomEntreprise(nomEntreprise);
        fournisseur.setSecteur(secteur);
        fournisseur.setDescription(description);
        fournisseur.setAdresse(adresse);
        fournisseur.setVille(ville);
        fournisseur.setTelephone(telephone);
        fournisseur.setActif(true);

        fournisseur = fournisseurRepository.save(fournisseur);

        // Générer le contrat automatiquement
        genererContrat(fournisseur);

        // Créer un service par défaut selon le secteur
        creerServiceParDefaut(fournisseur);

        searchService.refreshIndex();

        return fournisseur;
    }

    /**
     * Crée un service initial pour le fournisseur basé sur son secteur
     */
    private void creerServiceParDefaut(Fournisseur fournisseur) {
        ServiceFournisseur service = new ServiceFournisseur();
        service.setFournisseur(fournisseur);
        service.setSecteur(fournisseur.getSecteur());
        service.setDisponible(true);

        String nomService;
        String descService;
        Double prixInitial = 0.0;

        switch (fournisseur.getSecteur()) {
            case VOITURE:
                nomService = "Location de véhicule - " + fournisseur.getNomEntreprise();
                descService = "Service de location de voitures premium.";
                prixInitial = 25000.0;
                break;
            case ALIMENTAIRE:
                nomService = "Service Restauration / Traiteur";
                descService = "Découvrez nos menus gastronomiques.";
                prixInitial = 5000.0;
                break;
            case LOISIRS:
                nomService = "Activité de Loisirs & Détente";
                descService = "Profitez d'un moment inoubliable.";
                prixInitial = 10000.0;
                break;
            case EVENEMENTIEL:
                nomService = "Prestation Événementielle";
                descService = "Organisation et espace pour vos événements.";
                prixInitial = 50000.0;
                break;
            default:
                nomService = "Service Général";
                descService = "Prestation de service de qualité.";
        }

        service.setNom(nomService);
        service.setDescription(descService);
        service.setPrix(prixInitial);

        serviceFournisseurRepository.save(service);
    }

    /**
     * Génère un contrat pour le fournisseur
     */
    private void genererContrat(Fournisseur fournisseur) {
        ContratFournisseur contrat = new ContratFournisseur();
        contrat.setFournisseur(fournisseur);
        contrat.genererContenu();
        contratRepository.save(contrat);
    }

    // ==================== SERVICES FOURNISSEUR ====================

    public ServiceFournisseur ajouterService(Fournisseur fournisseur, ServiceFournisseur service) {
        service.setFournisseur(fournisseur);
        service.setSecteur(fournisseur.getSecteur());
        ServiceFournisseur s = serviceFournisseurRepository.save(service);
        searchService.refreshIndex();
        return s;
    }

    public List<ServiceFournisseur> getServicesByFournisseur(Fournisseur fournisseur) {
        return serviceFournisseurRepository.findByFournisseur(fournisseur);
    }

    public List<ServiceFournisseur> getServicesActifsByFournisseur(Fournisseur fournisseur) {
        return serviceFournisseurRepository.findByFournisseurAndDisponibleTrue(fournisseur);
    }

    public List<ServiceFournisseur> getAllServices() {
        return serviceFournisseurRepository.findAll();
    }

    public Optional<ServiceFournisseur> findServiceById(Long id) {
        return serviceFournisseurRepository.findById(id);
    }

    public ServiceFournisseur updateService(ServiceFournisseur service) {
        ServiceFournisseur s = serviceFournisseurRepository.save(service);
        searchService.refreshIndex();
        return s;
    }

    public void deleteService(Long serviceId) {
        serviceFournisseurRepository.deleteById(serviceId);
        searchService.refreshIndex();
    }

    // ==================== RECHERCHE ====================

    public List<Fournisseur> rechercher(String terme) {
        return fournisseurRepository.findByNomEntrepriseContainingIgnoreCase(terme);
    }

    public List<ServiceFournisseur> rechercherServices(String terme) {
        return serviceFournisseurRepository.findByNomContainingIgnoreCase(terme);
    }

    public List<ServiceFournisseur> getServicesBySecteur(Secteur secteur) {
        return serviceFournisseurRepository.findBySecteurAndDisponibleTrue(secteur);
    }

    public List<ServiceFournisseur> getServicesPopulaires(Secteur secteur) {
        return serviceFournisseurRepository.findMostPopularBySecteur(secteur);
    }

    // ==================== CONTRAT ====================

    public Optional<ContratFournisseur> getContrat(Fournisseur fournisseur) {
        return contratRepository.findByFournisseur(fournisseur);
    }

    public void signerContrat(Long fournisseurId) {
        Fournisseur fournisseur = fournisseurRepository.findById(fournisseurId)
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));

        ContratFournisseur contrat = contratRepository.findByFournisseur(fournisseur)
                .orElseThrow(() -> new RuntimeException("Contrat non trouvé"));

        contrat.signer();
        contratRepository.save(contrat);

        fournisseur.setContratAccepte(true);
        fournisseur.setDateContrat(LocalDateTime.now());
        fournisseurRepository.save(fournisseur);
    }

    // ==================== STATISTIQUES ====================

    public void updateFournisseur(ecom_blog.dto.AdminCreateFournisseurDto dto) {
        Fournisseur f = fournisseurRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Fournisseur non trouvé"));

        // Update User properties
        User u = f.getUser();
        u.setNom(dto.getNom());
        u.setTelephone(dto.getTelephone());
        // Email is usually not modifiable or requires extra care, but let's keep it
        // simple for now as requested.

        // Update Fournisseur properties
        f.setNomEntreprise(dto.getNomEntreprise());
        f.setSecteur(dto.getSecteur());
        f.setDescription(dto.getDescription());
        f.setAdresse(dto.getAdresse());
        f.setVille(dto.getVille());
        f.setTelephone(dto.getTelephone());

        fournisseurRepository.save(f);
    }

    public long countActifs() {
        return fournisseurRepository.countByActifTrue();
    }

    public long countByFournisseur(Long fournisseurId) {
        return serviceFournisseurRepository.countByFournisseurId(fournisseurId);
    }

    public long countBySecteur(Secteur secteur) {
        return fournisseurRepository.countBySecteur(secteur);
    }
}
