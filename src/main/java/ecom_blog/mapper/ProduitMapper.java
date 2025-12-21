package ecom_blog.mapper;

import ecom_blog.dto.CreateProduitDto;
import ecom_blog.dto.UpdateProduitDto;
import ecom_blog.model.Produit;
import org.springframework.stereotype.Component;

@Component
public class ProduitMapper {

    public Produit toEntity(CreateProduitDto dto) {
        if (dto == null) {
            return null;
        }

        Produit produit = new Produit();
        produit.setNom(dto.getNom());
        produit.setCategorie(dto.getCategorie());
        produit.setPrix(dto.getPrix());
        produit.setDescription(dto.getDescription());
        return produit;
    }

    public Produit toEntity(UpdateProduitDto dto) {
        if (dto == null) {
            return null;
        }

        Produit produit = new Produit();
        produit.setId(dto.getId());
        produit.setNom(dto.getNom());
        produit.setCategorie(dto.getCategorie());
        produit.setPrix(dto.getPrix());
        produit.setDescription(dto.getDescription());
        produit.setDisponible(dto.isDisponible());
        return produit;
    }
}
