package ecom_blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProduitDto {

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(min = 2, max = 150, message = "Le nom doit contenir entre 2 et 150 caractères")
    private String nom;

    @NotBlank(message = "La catégorie est obligatoire")
    @Size(min = 2, max = 100, message = "La catégorie doit contenir entre 2 et 100 caractères")
    private String categorie;

    @NotNull(message = "Le prix est obligatoire")
    @PositiveOrZero(message = "Le prix doit être supérieur ou égal à 0")
    private Double prix;

    @Size(max = 5000, message = "La description ne doit pas dépasser 5000 caractères")
    private String description;

    private boolean disponible = true;
}
