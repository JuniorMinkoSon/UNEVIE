package ecom_blog.config;

import ecom_blog.service.VisitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class VisitInterceptor implements HandlerInterceptor {

    @Autowired
    private VisitService visitService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();

        // Log visit only for page requests, avoid static resources
        if (!uri.contains("/css/") && !uri.contains("/js/") && !uri.contains("/images/") &&
                !uri.contains("/uploads/") && !uri.contains("/api/") && !uri.contains("/admin/")) {
            visitService.logVisit();
        }

        return true;
    }
}
