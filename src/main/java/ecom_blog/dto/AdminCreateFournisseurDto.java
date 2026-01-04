package ecom_blog.dto;

import ecom_blog.model.Secteur;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminCreateFournisseurDto {

    private Long id; // Null for creation, filled for update

    // User data
    @NotBlank(message = "L'email est requis")
    @Email(message = "Email invalide")
    private String email;

    @NotBlank(message = "Le nom du responsable est requis")
    private String nom;

    // Fournisseur data
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
