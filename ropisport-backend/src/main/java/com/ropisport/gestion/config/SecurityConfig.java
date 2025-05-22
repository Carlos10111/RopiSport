package com.ropisport.gestion.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Configuración de seguridad de la aplicación
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    /**
     * Filtro de autenticación JWT
     * @return filtro
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    /**
     * Proveedor de autenticación
     * @return proveedor de autenticación
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    /**
     * Administrador de autenticación
     * @param authConfig configuración de autenticación
     * @return administrador de autenticación
     * @throws Exception si ocurre un error
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Codificador de contraseñas
     * @return codificador de contraseñas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración de CORS
     * @return configuración de CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));   // Angular dev
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);          // solo si envías cookies / Authorization: Bearer
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    /**
     * Configuración del filtro de seguridad
     * @param http configuración de HTTP
     * @return configuración del filtro de seguridad
     * @throws Exception si ocurre un error
     */
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth ->
            auth.requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/error").permitAll()

                // Todas las rutas protegidas requieren rol de ADMIN
                .requestMatchers("/api/admin/**").hasRole(Constants.ROLE_ADMIN)
                .requestMatchers("/api/usuarios/**").hasRole(Constants.ROLE_ADMIN)
                .requestMatchers("/api/roles/**").hasRole(Constants.ROLE_ADMIN)
                .requestMatchers("/api/empresas/**").hasRole(Constants.ROLE_ADMIN)
                .requestMatchers("/api/instituciones/**").hasRole(Constants.ROLE_ADMIN)
                .requestMatchers("/api/tipo-instituciones/**").hasRole(Constants.ROLE_ADMIN)
                .requestMatchers("/api/categorias/**").hasRole(Constants.ROLE_ADMIN)
                .requestMatchers("/api/socias/**").hasRole(Constants.ROLE_ADMIN)
                .requestMatchers("/api/pagos/**").hasRole(Constants.ROLE_ADMIN)
                .requestMatchers("/api/pago-detalles/**").hasRole(Constants.ROLE_ADMIN)

                .anyRequest().authenticated()
        );

    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
}}