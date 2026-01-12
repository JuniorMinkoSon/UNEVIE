package ecom_blog.config;

import ecom_blog.security.CustomLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomLoginSuccessHandler successHandler;

    public SecurityConfig(CustomLoginSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                // ==== PUBLIC ====
                .requestMatchers(
                        "/", "/index", "/index.html", "/register",
                        "/blog/**", "/projets", "/services", "/objectifs",
                        "/propos", "/produits/**", "/contact", "/confirmation",
                        "/forgot-password")
                .permitAll()

                // ==== STATIC FILES ====
                .requestMatchers(
                        "/css/**", "/js/**", "/images/**", "/videos/**",
                        "/assets/**",
                        "/uploads/**")
                .permitAll()

                // ==== AUTH ====
                .requestMatchers("/login", "/admin/login").permitAll()

                // ==== ADMIN ====
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // ==== USER ====
                .requestMatchers("/user/**").hasRole("USER")

                // ==== OTHER ====
                .anyRequest().authenticated());

        http.formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler(successHandler)
                .failureUrl("/login?error")
                .permitAll());

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
