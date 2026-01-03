package ecom_blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                String projectDir = System.getProperty("user.dir");
                String uploadPath = "file:" + projectDir + "/src/main/resources/static/uploads/";

                // ✅ 1. Accès aux images uploadées (/uploads/**) dynamiquement
                registry.addResourceHandler("/uploads/**")
                                .addResourceLocations(uploadPath);

                // ✅ 2. Ressources statiques classiques
                registry.addResourceHandler("/static/**")
                                .addResourceLocations("classpath:/static/");
        }
}
