package ecom_blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanierItem extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Produit produit;
    private int quantite;

    public double getTotal() {
        return produit.getPrix() * quantite;
    }
}
