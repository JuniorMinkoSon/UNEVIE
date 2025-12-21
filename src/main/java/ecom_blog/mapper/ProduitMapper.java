package ecom_blog.mapper;

import ecom_blog.dto.CreateProduitDto;
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
}
