package ecom_blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateArticleDto {

    @NotBlank(message = "Le titre est obligatoire")
    @Size(min = 2, max = 200, message = "Le titre doit contenir entre 2 et 200 caractères")
    private String titre;

    @NotNull(message = "La catégorie est obligatoire")
    private Long category;

    @Size(max = 5000, message = "Le contenu ne doit pas dépasser 5000 caractères")
    private String contenu;
}
