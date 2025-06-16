package com.ropisport.gestion.config;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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

import com.ropisport.gestion.security.jwt.AuthEntryPointJwt;
import com.ropisport.gestion.security.jwt.AuthTokenFilter;
import com.ropisport.gestion.security.service.UserDetailsServiceImpl;
import com.ropisport.gestion.util.Constants;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
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
        http.csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/error").permitAll()

                // Solo ADMIN general - SIN prefijo ROLE_
                .requestMatchers("/api/admin/**").hasRole(Constants.ROLE_ADMIN)
                .requestMatchers("/api/usuarios/**").hasRole(Constants.ROLE_ADMIN)
                .requestMatchers("/api/roles/**").hasRole(Constants.ROLE_ADMIN)
                
                // ADMIN general O ADMIN_SOCIAS - SIN prefijo ROLE_
                .requestMatchers("/api/empresas/**").hasAnyRole(Constants.ROLE_ADMIN, Constants.ROLE_ADMIN_SOCIAS)
                .requestMatchers("/api/instituciones/**").hasAnyRole(Constants.ROLE_ADMIN, Constants.ROLE_ADMIN_SOCIAS)
                .requestMatchers("/api/tipo-instituciones/**").hasAnyRole(Constants.ROLE_ADMIN, Constants.ROLE_ADMIN_SOCIAS)
                .requestMatchers("/api/categorias/**").hasAnyRole(Constants.ROLE_ADMIN, Constants.ROLE_ADMIN_SOCIAS)
                .requestMatchers("/api/socias/**").hasAnyRole(Constants.ROLE_ADMIN, Constants.ROLE_ADMIN_SOCIAS)
                .requestMatchers("/api/pagos/**").hasAnyRole(Constants.ROLE_ADMIN, Constants.ROLE_ADMIN_SOCIAS)
                .requestMatchers("/api/pago-detalles/**").hasAnyRole(Constants.ROLE_ADMIN, Constants.ROLE_ADMIN_SOCIAS)

                .anyRequest().authenticated()
            );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}