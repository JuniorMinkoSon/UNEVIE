package ecom_blog.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EquipeFiestaDto {

    private Long id;

    @NotNull(message = "Le nom de l'équipe est obligatoire")
    private String nomEquipe;

    private Long createurId;
    private String createurNom;

    @NotNull(message = "La date de l'événement est obligatoire")
    private LocalDate dateEvenement;

    private List<Long> entrepriseIds;
    private List<EntrepriseDto> entreprises;

    private Double budgetTotal;
    private String notes;

    private List<Long> membreIds;
}
