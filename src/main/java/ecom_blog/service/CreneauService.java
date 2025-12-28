package ecom_blog.service;

import ecom_blog.dto.CreneauDto;
import ecom_blog.model.CreneauDisponibilite;
import ecom_blog.model.Produit;
import ecom_blog.model.enums.StatutCreneau;
import ecom_blog.repository.CreneauDisponibiliteRepository;
import ecom_blog.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreneauService {

    private final CreneauDisponibiliteRepository creneauRepository;
    private final ProduitRepository produitRepository;

    /**
     * Récupère tous les créneaux disponibles pour un produit
     */
    public List<CreneauDto> getCreneauxDisponibles(Long produitId) {
        return creneauRepository.findByProduit_IdAndStatut(produitId, StatutCreneau.DISPONIBLE)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Recherche intelligente de créneaux disponibles dans une période
     */
    public List<CreneauDto> searchAvailableSlots(Long produitId, LocalDateTime debut, LocalDateTime fin) {
        return creneauRepository.findAvailableSlots(produitId, debut, fin)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Crée un nouveau créneau de disponibilité
     */
    @Transactional
    public CreneauDto createCreneau(CreneauDto dto) {
        // Vérifier qu'il n'y a pas de conflit
        List<CreneauDisponibilite> conflits = creneauRepository.findConflictingSlots(
                dto.getProduitId(),
                dto.getDateDebut(),
                dto.getDateFin());

        if (!conflits.isEmpty()) {
            throw new RuntimeException("Conflit détecté : un créneau existe déjà pour cette période");
        }

        Produit produit = produitRepository.findById(dto.getProduitId())
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        CreneauDisponibilite creneau = new CreneauDisponibilite();
        creneau.setProduit(produit);
        creneau.setDateDebut(dto.getDateDebut());
        creneau.setDateFin(dto.getDateFin());
        creneau.setStatut(StatutCreneau.DISPONIBLE);
        creneau.setNotes(dto.getNotes());

        return toDto(creneauRepository.save(creneau));
    }

    /**
     * Réserve un créneau (appelé lors de la création d'une commande)
     */
    @Transactional
    public void reserverCreneau(Long creneauId, Long commandeId) {
        CreneauDisponibilite creneau = creneauRepository.findById(creneauId)
                .orElseThrow(() -> new RuntimeException("Créneau non trouvé"));

        if (creneau.getStatut() != StatutCreneau.DISPONIBLE) {
            throw new RuntimeException("Ce créneau n'est pas disponible");
        }

        creneau.setStatut(StatutCreneau.RESERVE);
        // Note: La relation commande sera définie côté Commande
        creneauRepository.save(creneau);
    }

    /**
     * Libère un créneau (annulation de commande)
     */
    @Transactional
    public void libererCreneau(Long creneauId) {
        CreneauDisponibilite creneau = creneauRepository.findById(creneauId)
                .orElseThrow(() -> new RuntimeException("Créneau non trouvé"));

        creneau.setStatut(StatutCreneau.DISPONIBLE);
        creneauRepository.save(creneau);
    }

    /**
     * Bloque un créneau (maintenance, etc.)
     */
    @Transactional
    public void bloquerCreneau(Long creneauId, String notes) {
        CreneauDisponibilite creneau = creneauRepository.findById(creneauId)
                .orElseThrow(() -> new RuntimeException("Créneau non trouvé"));

        creneau.setStatut(StatutCreneau.BLOQUE);
        creneau.setNotes(notes);
        creneauRepository.save(creneau);
    }

    /**
     * Génération automatique de créneaux pour un produit
     * (Ex: créer des créneaux de 24h pour les 30 prochains jours)
     */
    @Transactional
    public void genererCreneauxAutomatiques(Long produitId, int nombreJours) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        LocalDateTime debut = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0);

        for (int i = 0; i < nombreJours; i++) {
            LocalDateTime dateDebut = debut.plusDays(i);
            LocalDateTime dateFin = dateDebut.plusDays(1);

            // Vérifier qu'il n'existe pas déjà
            List<CreneauDisponibilite> existants = creneauRepository.findConflictingSlots(
                    produitId, dateDebut, dateFin);

            if (existants.isEmpty()) {
                CreneauDisponibilite creneau = new CreneauDisponibilite();
                creneau.setProduit(produit);
                creneau.setDateDebut(dateDebut);
                creneau.setDateFin(dateFin);
                creneau.setStatut(StatutCreneau.DISPONIBLE);
                creneauRepository.save(creneau);
            }
        }
    }

    private CreneauDto toDto(CreneauDisponibilite creneau) {
        CreneauDto dto = new CreneauDto();
        dto.setId(creneau.getId());
        dto.setProduitId(creneau.getProduit().getId());
        dto.setProduitNom(creneau.getProduit().getNom());
        dto.setDateDebut(creneau.getDateDebut());
        dto.setDateFin(creneau.getDateFin());
        dto.setStatut(creneau.getStatut());
        dto.setNotes(creneau.getNotes());

        if (creneau.getCommande() != null) {
            dto.setCommandeId(creneau.getCommande().getId());
        }

        return dto;
    }
}
