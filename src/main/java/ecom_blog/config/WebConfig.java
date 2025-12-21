package ecom_blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // ✅ 1. Permet à Spring d'accéder au dossier /uploads/
        // Path adjusted for current user GUEYE
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:C:/Users/DELL PRECISION 5550/Downloads/ecom-vente/ecom-vente/src/main/resources/static/uploads/");
//                .addResourceLocations("file:C:/Users/GUEYE/Desktop/projet/UNEVIE/src/main/resources/static/uploads/");
        // ✅ 2. (Optionnel) pour les ressources de base
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
