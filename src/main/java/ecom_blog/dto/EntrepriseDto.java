package ecom_blog.dto;

import ecom_blog.model.enums.CategorieEntreprise;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EntrepriseDto {

    private Long id;

    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    private String nom;

    private String description;
    private String logo;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    private String email;
    private String adresse;

    @NotNull(message = "La catégorie est obligatoire")
    private CategorieEntreprise categorie;

    private Long quartierId;
    private String quartierNom;

    private Double latitude;
    private Double longitude;

    // Contrat EasyService
    private Boolean contratActif;
    private LocalDate dateDebutContrat;
    private LocalDate dateFinContrat;
    private String notesContrat;
    private Double commissionPourcentage;

    // Stats
    private Double noteGlobale;
    private Integer nombreAvis;
    private Integer nombreProduits;
}
