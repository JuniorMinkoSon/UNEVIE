package ecom_blog.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                // ✅ Redirige les admins vers le tableau de bord
                response.sendRedirect("/admin/dashboard");
                return;
            } else if (role.equals("ROLE_USER")) {
                // ✅ Redirige les utilisateurs simples vers la page d’accueil dynamique ("/")
                response.sendRedirect("/");
                return;
            }
        }

        // Si aucun rôle reconnu → redirection par défaut
        response.sendRedirect("/login?error");
    }
}
