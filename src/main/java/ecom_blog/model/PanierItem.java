package ecom_blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanierItem {
    private Produit produit;
    private int quantite;

    public double getTotal() {
        return produit.getPrix() * quantite;
    }
}
