package ecom_blog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuartierDto {

    private Long id;

    @NotBlank(message = "Le nom du quartier est obligatoire")
    private String nom;

    @NotBlank(message = "La commune est obligatoire")
    private String commune;

    private Double latitude;
    private Double longitude;
    private Double rayonCouverture;
}
