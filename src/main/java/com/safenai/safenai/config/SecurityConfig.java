package com.safenai.safenai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // ❗ Disable CSRF (Enable in production)
            // Enable CORS (Cross-Origin Resource Sharing)
            .cors(cors -> cors.disable()) // ❗ Disable CORS (Enable in production)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll() // api routes
                .anyRequest().authenticated() // Secure other routes
            )
            .httpBasic(); // Use Basic Authentication
        
        return http.build();
    }

//     @Configuration
// public class CorsConfig implements WebMvcConfigurer {
    
//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//             .allowedOrigins(
//                 "http://localhost:8081",  // For local development
//                 "capacitor://localhost",   // For Capacitor/Ionic apps
//                 "ionic://localhost",       // For Ionic apps
//                 "http://localhost",        // General local testing
//                 "*"                        // Consider for development only
//             )
//             .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//             .allowedHeaders("*")
//             .allowCredentials(true);
//     }
// }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:8081", "capacitor://localhost", "ionic://localhost", "http://localhost")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
    // Add any other security configurations here as needed
}