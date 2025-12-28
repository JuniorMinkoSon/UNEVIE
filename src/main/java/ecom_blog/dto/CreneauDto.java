package ecom_blog.dto;

import ecom_blog.model.enums.StatutCreneau;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreneauDto {

    private Long id;

    @NotNull(message = "L'ID du produit est obligatoire")
    private Long produitId;

    private String produitNom;

    @NotNull(message = "La date de début est obligatoire")
    private LocalDateTime dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    private LocalDateTime dateFin;

    private StatutCreneau statut;
    private Long commandeId;
    private String notes;
}
