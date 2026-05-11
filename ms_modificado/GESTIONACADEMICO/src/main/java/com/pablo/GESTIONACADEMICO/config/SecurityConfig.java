package com.pablo.GESTIONACADEMICO.config;

import com.pablo.GESTIONACADEMICO.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // ── Llamadas internas entre microservicios (sin token) ──
                // AsistenciaService consulta /asignaciones/{id} para obtener nombreAsignatura
                .requestMatchers(HttpMethod.GET, "/asignaciones/**").permitAll()
                // NotaService y otros consultan asignaturas libremente
                .requestMatchers(HttpMethod.GET, "/asignaturas/**").permitAll()
                // ── Todo lo demás requiere token del usuario ──
                .requestMatchers(HttpMethod.POST, "/asignaturas").authenticated()
                .requestMatchers(HttpMethod.POST, "/asignaciones").authenticated()
                .requestMatchers(HttpMethod.POST, "/evaluaciones").authenticated()
                .requestMatchers(HttpMethod.GET, "/evaluaciones/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/notas").authenticated()
                .requestMatchers(HttpMethod.GET, "/notas/**").authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",
            "http://localhost:4200",
            "http://localhost:5173",
            "http://localhost:8080"
        ));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
