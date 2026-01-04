package ecom_blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateServiceDto {

    @NotBlank(message = "Le nom du service est requis")
    private String nom;

    private String description;

    @NotNull(message = "Le prix est requis")
    private Double prix;

    private Double prixParJour;

    private Integer duree;

    private Integer capacite;
}
