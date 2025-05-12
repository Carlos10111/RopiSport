package com.ropisport.gestion.security.jwt;

import com.ropisport.gestion.util.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utilidades para JWT (generación y validación de tokens)
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${ropisport.app.jwtSecret}")
    private String jwtSecret;

    @Value("${ropisport.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Genera un token JWT a partir de la autenticación
     * @param authentication objeto Authentication de Spring Security
     * @return token JWT
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * Obtiene el nombre de usuario a partir de un token JWT
     * @param token token JWT
     * @return nombre de usuario
     */
    public String getUsernameFromJwtToken(String token) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Valida un token JWT
     * @param authToken token JWT
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("La cadena claims de JWT está vacía: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error al validar token JWT: {}", e.getMessage());
        }

        return false;
    }
    
    /**
     * Extrae el token JWT del header Authorization
     * @param authHeader valor del header Authorization
     * @return token JWT o null si no es válido
     */
    public String parseJwt(String authHeader) {
        if (authHeader != null && authHeader.startsWith(Constants.JWT_PREFIX)) {
            return authHeader.substring(Constants.JWT_PREFIX.length());
        }
        return null;
    }
}