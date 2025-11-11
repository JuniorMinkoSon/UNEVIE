package ecom_blog.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L; // ✅ Bonnes pratiques

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nom;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role; // ROLE_USER, ROLE_ADMIN

    // Getters & Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
