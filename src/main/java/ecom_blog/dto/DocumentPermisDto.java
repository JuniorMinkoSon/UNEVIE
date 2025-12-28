package ecom_blog.dto;

import ecom_blog.model.enums.StatutPermis;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class DocumentPermisDto {

    private Long id;
    private Long commandeId;
    private String cheminFichier;
    private String numeroPermis;
    private LocalDate dateExpiration;
    private StatutPermis statut;
    private LocalDateTime dateUpload;
    private LocalDateTime dateVerification;
    private String verifieParNom;
    private String commentaireRefus;
}
