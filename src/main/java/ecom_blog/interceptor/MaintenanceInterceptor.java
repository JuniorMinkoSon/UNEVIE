package ecom_blog.interceptor;

import ecom_blog.service.SettingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class MaintenanceInterceptor implements HandlerInterceptor {

    private final SettingService settingService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI();

        // 1. Toujours autoriser l'admin, la page de maintenance, l'API et le login
        if (uri.startsWith("/admin") || uri.startsWith("/maintenance") ||
                uri.startsWith("/login") || uri.startsWith("/api") || uri.startsWith("/logout")) {
            return true;
        }

        // 2. Vérifier si l'utilisateur est Admin (via SecurityContext)
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }

        // 3. Appliquer la maintenance si active
        if (settingService.isMaintenanceMode()) {
            response.sendRedirect("/maintenance");
            return false;
        }

        return true;
    }
}
