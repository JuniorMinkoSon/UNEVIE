package ecom_blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateReservationDto {

    @NotNull(message = "Le service est requis")
    private Long serviceId;

    @NotBlank(message = "Votre nom est requis")
    private String nomClient;

    @NotBlank(message = "Votre téléphone est requis")
    private String telephone;

    private String email;

    @NotNull(message = "La date du service est requise")
    private LocalDate dateService;

    private LocalDate dateFinService;

    private Integer nombrePersonnes;

    private Integer nombreJours;

    private boolean avecChauffeur;

    private String notes;

    private String modePaiement;

    private String operateur;
}
