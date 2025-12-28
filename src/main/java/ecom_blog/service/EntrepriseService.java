package ecom_blog.service;

import ecom_blog.dto.EntrepriseDto;
import ecom_blog.model.Entreprise;
import ecom_blog.model.Quartier;
import ecom_blog.model.enums.CategorieEntreprise;
import ecom_blog.repository.EntrepriseRepository;
import ecom_blog.repository.QuartierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntrepriseService {

    private final EntrepriseRepository entrepriseRepository;
    private final QuartierRepository quartierRepository;

    public List<EntrepriseDto> getAllEntreprises() {
        return entrepriseRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<EntrepriseDto> getEntreprisesByCategorie(CategorieEntreprise categorie) {
        return entrepriseRepository.findByCategorie(categorie).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<EntrepriseDto> getEntreprisesByQuartier(Long quartierId) {
        return entrepriseRepository.findByQuartier_Id(quartierId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<EntrepriseDto> getEntreprisesByFilters(CategorieEntreprise categorie, Long quartierId) {
        if (categorie != null && quartierId != null) {
            return entrepriseRepository.findByCategorieAndQuartier_Id(categorie, quartierId).stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        } else if (categorie != null) {
            return getEntreprisesByCategorie(categorie);
        } else if (quartierId != null) {
            return getEntreprisesByQuartier(quartierId);
        } else {
            return getAllEntreprises();
        }
    }

    public List<EntrepriseDto> getActiveEntreprises() {
        return entrepriseRepository.findByContratActifTrue().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public EntrepriseDto getEntrepriseById(Long id) {
        Entreprise entreprise = entrepriseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));
        return toDto(entreprise);
    }

    @Transactional
    public EntrepriseDto createEntreprise(EntrepriseDto dto) {
        Entreprise entreprise = new Entreprise();
        updateEntityFromDto(entreprise, dto);
        return toDto(entrepriseRepository.save(entreprise));
    }

    @Transactional
    public EntrepriseDto updateEntreprise(Long id, EntrepriseDto dto) {
        Entreprise entreprise = entrepriseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvée"));
        updateEntityFromDto(entreprise, dto);
        return toDto(entrepriseRepository.save(entreprise));
    }

    @Transactional
    public void deleteEntreprise(Long id) {
        entrepriseRepository.deleteById(id);
    }

    private void updateEntityFromDto(Entreprise entreprise, EntrepriseDto dto) {
        entreprise.setNom(dto.getNom());
        entreprise.setDescription(dto.getDescription());
        entreprise.setLogo(dto.getLogo());
        entreprise.setTelephone(dto.getTelephone());
        entreprise.setEmail(dto.getEmail());
        entreprise.setAdresse(dto.getAdresse());
        entreprise.setCategorie(dto.getCategorie());
        entreprise.setLatitude(dto.getLatitude());
        entreprise.setLongitude(dto.getLongitude());
        entreprise.setContratActif(dto.getContratActif() != null ? dto.getContratActif() : false);
        entreprise.setDateDebutContrat(dto.getDateDebutContrat());
        entreprise.setDateFinContrat(dto.getDateFinContrat());
        entreprise.setNotesContrat(dto.getNotesContrat());
        entreprise.setCommissionPourcentage(
                dto.getCommissionPourcentage() != null ? dto.getCommissionPourcentage() : 10.0);

        if (dto.getQuartierId() != null) {
            Quartier quartier = quartierRepository.findById(dto.getQuartierId())
                    .orElse(null);
            entreprise.setQuartier(quartier);
        }
    }

    private EntrepriseDto toDto(Entreprise entreprise) {
        EntrepriseDto dto = new EntrepriseDto();
        dto.setId(entreprise.getId());
        dto.setNom(entreprise.getNom());
        dto.setDescription(entreprise.getDescription());
        dto.setLogo(entreprise.getLogo());
        dto.setTelephone(entreprise.getTelephone());
        dto.setEmail(entreprise.getEmail());
        dto.setAdresse(entreprise.getAdresse());
        dto.setCategorie(entreprise.getCategorie());
        dto.setLatitude(entreprise.getLatitude());
        dto.setLongitude(entreprise.getLongitude());
        dto.setContratActif(entreprise.getContratActif());
        dto.setDateDebutContrat(entreprise.getDateDebutContrat());
        dto.setDateFinContrat(entreprise.getDateFinContrat());
        dto.setNotesContrat(entreprise.getNotesContrat());
        dto.setCommissionPourcentage(entreprise.getCommissionPourcentage());
        dto.setNoteGlobale(entreprise.getNoteGlobale());
        dto.setNombreAvis(entreprise.getNombreAvis());

        if (entreprise.getQuartier() != null) {
            dto.setQuartierId(entreprise.getQuartier().getId());
            dto.setQuartierNom(entreprise.getQuartier().getNom());
        }

        if (entreprise.getProduits() != null) {
            dto.setNombreProduits(entreprise.getProduits().size());
        }

        return dto;
    }
}
