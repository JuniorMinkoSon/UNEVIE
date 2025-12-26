package ecom_blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // ✅ 1. Accès aux images uploadées (/uploads/**)
                String userDir = System.getProperty("user.dir");
                registry.addResourceHandler("/uploads/**")
                                .addResourceLocations("file:" + userDir + "/src/main/resources/static/uploads/");

                // ✅ 2. Ressources statiques classiques
                registry.addResourceHandler("/static/**", "/**")
                                .addResourceLocations("classpath:/static/", "classpath:/public/",
                                                "classpath:/resources/", "classpath:/META-INF/resources/");
        }

        @Bean
        public LocaleResolver localeResolver() {
                org.springframework.web.servlet.i18n.SessionLocaleResolver slr = new org.springframework.web.servlet.i18n.SessionLocaleResolver();
                slr.setDefaultLocale(Locale.FRENCH);
                return slr;
        }

        @Bean
        public LocaleChangeInterceptor localeChangeInterceptor() {
                LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
                lci.setParamName("lang");
                return lci;
        }

        private final ecom_blog.interceptor.MaintenanceInterceptor maintenanceInterceptor;

        public WebConfig(ecom_blog.interceptor.MaintenanceInterceptor maintenanceInterceptor) {
                this.maintenanceInterceptor = maintenanceInterceptor;
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(localeChangeInterceptor());
                registry.addInterceptor(maintenanceInterceptor)
                                .addPathPatterns("/**")
                                .excludePathPatterns(
                                                "/css/**",
                                                "/js/**",
                                                "/images/**",
                                                "/uploads/**",
                                                "/static/**",
                                                "/webjars/**",
                                                "/favicon.ico",
                                                "/maintenance",
                                                "/admin/**",
                                                "/login",
                                                "/api/**");
        }

        @Bean
        public org.springframework.context.MessageSource messageSource() {
                org.springframework.context.support.ResourceBundleMessageSource messageSource = new org.springframework.context.support.ResourceBundleMessageSource();
                messageSource.setBasename("messages");
                messageSource.setDefaultEncoding("UTF-8");
                messageSource.setUseCodeAsDefaultMessage(true);
                return messageSource;
        }
}
