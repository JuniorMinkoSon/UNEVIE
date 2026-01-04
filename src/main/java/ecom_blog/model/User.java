package ecom_blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(unique = true, nullable = false)
    private String email;

    private String nom;

    private String password;

    private String telephone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean mustChangePassword = false;

    // ================= LIVREUR / FOURNISSEUR =================
    private boolean actif = true;
    private boolean disponible = true;

    // GPS simulé
    private Double latitude;
    private Double longitude;

    // Optimisation middleware
    private Double congestionScore;
    private Double consommationEstimee;
}
