package ecom_blog.service;

import ecom_blog.model.Commande;
import ecom_blog.model.CreneauHoraire;
import ecom_blog.model.User;
import ecom_blog.repository.CreneauHoraireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * CreneauHoraireService - Gestion des créneaux de disponibilité
 */
@Service
public class CreneauHoraireService {

    @Autowired
    private CreneauHoraireRepository creneauRepository;

    /**
     * Récupère les créneaux disponibles d'un fournisseur
     */
    public List<CreneauHoraire> getCreneauxDisponibles(User fournisseur) {
        LocalDateTime maintenant = LocalDateTime.now();
        return creneauRepository.findByFournisseurAndDisponibleTrueAndDebutAfter(
                fournisseur, maintenant);
    }

    /**
     * Vérifie si un créneau est disponible
     */
    public boolean verifierDisponibilite(User fournisseur, LocalDateTime debut, LocalDateTime fin) {
        Optional<CreneauHoraire> creneau = creneauRepository
                .findByFournisseurAndDebutAndFin(fournisseur, debut, fin);

        return creneau.isPresent() && creneau.get().isDisponible();
    }

    /**
     * Réserve un créneau pour une commande
     */
    @Transactional
    public void reserverCreneau(CreneauHoraire creneau, Commande commande) {
        creneau.setReserve(true);
        creneau.setDisponible(false);
        creneau.setCommande(commande);
        creneauRepository.save(creneau);
    }

    /**
     * Libère un créneau (en cas d'annulation)
     */
    @Transactional
    public void libererCreneau(CreneauHoraire creneau) {
        creneau.setReserve(false);
        creneau.setDisponible(true);
        creneau.setCommande(null);
        creneauRepository.save(creneau);
    }

    /**
     * Crée un nouveau créneau
     */
    @Transactional
    public CreneauHoraire creerCreneau(CreneauHoraire creneau) {
        return creneauRepository.save(creneau);
    }

    /**
     * Récupère tous les créneaux d'un fournisseur
     */
    public List<CreneauHoraire> getCreneauxFournisseur(User fournisseur) {
        return creneauRepository.findByFournisseurOrderByDebutAsc(fournisseur);
    }

    /**
     * Trouve un créneau par ID
     */
    public Optional<CreneauHoraire> findById(Long id) {
        return creneauRepository.findById(id);
    }

    /**
     * Supprime un créneau
     */
    @Transactional
    public void supprimerCreneau(Long id) {
        creneauRepository.deleteById(id);
    }
}
