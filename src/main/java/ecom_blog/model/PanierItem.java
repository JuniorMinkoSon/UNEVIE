package ecom_blog.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanierItem extends BaseEntity{
    private Produit produit;
    private int quantite;

    public double getTotal() {
        return produit.getPrix() * quantite;
    }
}
