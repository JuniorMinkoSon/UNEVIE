package ecom_blog.service;

import ecom_blog.dto.EquipeFiestaDto;
import ecom_blog.dto.EntrepriseDto;
import ecom_blog.model.EquipeFiesta;
import ecom_blog.model.Entreprise;
import ecom_blog.model.User;
import ecom_blog.repository.EquipeFiestaRepository;
import ecom_blog.repository.EntrepriseRepository;
import ecom_blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipeFiestaService {

    private final EquipeFiestaRepository equipeFiestaRepository;
    private final UserRepository userRepository;
    private final EntrepriseRepository entrepriseRepository;

    public List<EquipeFiestaDto> getEquipesByUser(Long userId) {
        return equipeFiestaRepository.findAllByUser(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public EquipeFiestaDto getEquipeById(Long id) {
        EquipeFiesta equipe = equipeFiestaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Équipe Fiesta non trouvée"));
        return toDto(equipe);
    }

    @Transactional
    public EquipeFiestaDto createEquipe(EquipeFiestaDto dto, Long userId) {
        User createur = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        EquipeFiesta equipe = new EquipeFiesta();
        equipe.setNomEquipe(dto.getNomEquipe());
        equipe.setCreateur(createur);
        equipe.setDateEvenement(dto.getDateEvenement());
        equipe.setNotes(dto.getNotes());

        // Ajouter les entreprises sélectionnées
        if (dto.getEntrepriseIds() != null && !dto.getEntrepriseIds().isEmpty()) {
            List<Entreprise> entreprises = entrepriseRepository.findAllById(dto.getEntrepriseIds());
            equipe.setEntreprises(entreprises);

            // Calculer le budget total (somme des services des entreprises)
            // Pour l'instant on met 0, à affiner selon la logique métier
            equipe.setBudgetTotal(dto.getBudgetTotal() != null ? dto.getBudgetTotal() : 0.0);
        }

        // Ajouter les membres si spécifiés
        if (dto.getMembreIds() != null && !dto.getMembreIds().isEmpty()) {
            List<User> membres = userRepository.findAllById(dto.getMembreIds());
            equipe.setMembres(membres);
        }

        return toDto(equipeFiestaRepository.save(equipe));
    }

    @Transactional
    public EquipeFiestaDto updateEquipe(Long id, EquipeFiestaDto dto) {
        EquipeFiesta equipe = equipeFiestaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Équipe Fiesta non trouvée"));

        equipe.setNomEquipe(dto.getNomEquipe());
        equipe.setDateEvenement(dto.getDateEvenement());
        equipe.setNotes(dto.getNotes());
        equipe.setBudgetTotal(dto.getBudgetTotal());

        if (dto.getEntrepriseIds() != null) {
            List<Entreprise> entreprises = entrepriseRepository.findAllById(dto.getEntrepriseIds());
            equipe.setEntreprises(entreprises);
        }

        if (dto.getMembreIds() != null) {
            List<User> membres = userRepository.findAllById(dto.getMembreIds());
            equipe.setMembres(membres);
        }

        return toDto(equipeFiestaRepository.save(equipe));
    }

    @Transactional
    public void deleteEquipe(Long id) {
        equipeFiestaRepository.deleteById(id);
    }

    private EquipeFiestaDto toDto(EquipeFiesta equipe) {
        EquipeFiestaDto dto = new EquipeFiestaDto();
        dto.setId(equipe.getId());
        dto.setNomEquipe(equipe.getNomEquipe());
        dto.setCreateurId(equipe.getCreateur().getId());
        dto.setCreateurNom(equipe.getCreateur().getEmail());
        dto.setDateEvenement(equipe.getDateEvenement());
        dto.setBudgetTotal(equipe.getBudgetTotal());
        dto.setNotes(equipe.getNotes());

        if (equipe.getEntreprises() != null) {
            dto.setEntrepriseIds(equipe.getEntreprises().stream()
                    .map(Entreprise::getId)
                    .collect(Collectors.toList()));

            // Conversion simplifiée des entreprises
            dto.setEntreprises(equipe.getEntreprises().stream()
                    .map(e -> {
                        EntrepriseDto eDto = new EntrepriseDto();
                        eDto.setId(e.getId());
                        eDto.setNom(e.getNom());
                        eDto.setDescription(e.getDescription());
                        eDto.setCategorie(e.getCategorie());
                        eDto.setLogo(e.getLogo());
                        return eDto;
                    })
                    .collect(Collectors.toList()));
        }

        if (equipe.getMembres() != null) {
            dto.setMembreIds(equipe.getMembres().stream()
                    .map(User::getId)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}
