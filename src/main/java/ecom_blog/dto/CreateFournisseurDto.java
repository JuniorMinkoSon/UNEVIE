package ecom_blog.dto;

import ecom_blog.model.Secteur;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateFournisseurDto {

    @NotBlank(message = "Le nom de l'entreprise est requis")
    private String nomEntreprise;

    @NotNull(message = "Le secteur d'activité est requis")
    private Secteur secteur;

    private String description;

    @NotBlank(message = "L'adresse est requise")
    private String adresse;

    private String ville;

    @NotBlank(message = "Le téléphone est requis")
    private String telephone;
}
