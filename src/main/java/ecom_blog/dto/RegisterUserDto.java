package ecom_blog.dto;

public class RegisterUserDto {
    private String email;
    private String nom;
    private String password;
    private String telephone;

    // Nouveaux champs pour la gestion des rôles
    private String accountType = "CLIENT"; // CLIENT ou PRESTATAIRE (Par défaut CLIENT)
    private String typePrestataire; // CHAUFFEUR, LIVREUR...

    // --- Getters et Setters Manuels (Pour éviter tout problème Lombok) ---

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getTypePrestataire() {
        return typePrestataire;
    }

    public void setTypePrestataire(String typePrestataire) {
        this.typePrestataire = typePrestataire;
    }

    @Override
    public String toString() {
        return "RegisterUserDto{" +
                "email='" + email + '\'' +
                ", nom='" + nom + '\'' +
                ", telephone='" + telephone + '\'' +
                ", accountType='" + accountType + '\'' +
                ", typePrestataire='" + typePrestataire + '\'' +
                '}';
    }
}