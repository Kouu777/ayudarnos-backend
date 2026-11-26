package cl.ayudarnos.api.config;

import cl.ayudarnos.api.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${app.cors.allowed-origins:*}")
    private String allowedOrigins;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Auth endpoints
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        // Public endpoints to browse needs and catastrophes on homepage
                        .requestMatchers(HttpMethod.GET, "/api/v1/needs/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/catastrophes/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/organizations/**").permitAll()

                        // Public donation intention submission, tracking & form templates
                        .requestMatchers(HttpMethod.POST, "/api/v1/donations").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/donations/track/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/donations/form-template/**").permitAll()

                        // Módulo de Donaciones Grupales (público para crear iniciativa, aportar y rastrear)
                        .requestMatchers(HttpMethod.POST, "/api/v1/donations/group").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/donations/group/track/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/donations/group/*/contributions").permitAll()

                        // Módulo de Voluntariado (público para ver convocatorias e inscribirse)
                        .requestMatchers(HttpMethod.GET, "/api/v1/volunteering/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/volunteering/*/register").permitAll()

                        // Sistema de Alertas (público para ver alertas críticas activas y suscribirse)
                        .requestMatchers(HttpMethod.GET, "/api/v1/alerts/active").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/alerts/subscriptions").permitAll()

                        // Swagger / OpenAPI documentation
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Error handler
                        .requestMatchers("/error").permitAll()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        if ("*".equals(allowedOrigins)) {
            configuration.setAllowedOriginPatterns(List.of("*"));
        } else {
            configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        }
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
