package ecom_blog.security;

import ecom_blog.model.Role;
import ecom_blog.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // 🔐 Autorités Spring Security (ADMIN / USER)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Role role = user.getRole(); // ENUM Role

        if (role == null) {
            return Collections.emptyList();
        }

        // ⚠️ ICI ON NE RAJOUTE PAS "ROLE_" SI L’ENUM LE CONTIENT DÉJÀ
        return Collections.singleton(
                new SimpleGrantedAuthority(role.name())
        );
    }

    // 🔑 Mot de passe
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 👤 Identifiant (email)
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // ✅ États du compte
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // 🔁 Accès à l’utilisateur métier
    public User getUser() {
        return user;
    }
}
