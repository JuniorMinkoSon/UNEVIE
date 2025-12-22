package ecom_blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // ✅ 1. Accès aux images uploadées (/uploads/**)
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(
                        "file:C:/Users/DELL PRECISION 5550/Downloads/barikaweb-clean/src/main/resources/static/uploads/"
                );

        // ✅ 2. Ressources statiques classiques
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
