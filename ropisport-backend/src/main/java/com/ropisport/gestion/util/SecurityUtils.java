package com.ropisport.gestion.util;

import com.ropisport.gestion.security.jwt.UserDetailsImpl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utilidades para manejo de seguridad
 */
public final class SecurityUtils {
    
    private SecurityUtils() {
        // Constructor privado para evitar instanciación
    }
    
    /**
     * Obtiene el ID del usuario autenticado
     * @return ID del usuario o null si no hay usuario autenticado
     */
    public static Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || 
                !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            return null;
        }
        
        return ((UserDetailsImpl) authentication.getPrincipal()).getId();
    }
    
    /**
     * Obtiene el nombre de usuario del usuario autenticado
     * @return nombre de usuario o null si no hay usuario autenticado
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        return authentication.getName();
    }
    
    /**
     * Verifica si el usuario actual tiene el rol especificado
     * @param role nombre del rol (sin el prefijo ROLE_)
     * @return true si tiene el rol, false en caso contrario
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Constants.ROLE_PREFIX + role));
    }
    
    /**
     * Verifica si el usuario actual es administrador
     * @return true si es administrador, false en caso contrario
     */
    public static boolean isAdmin() {
        return hasRole(Constants.ROLE_ADMIN);
    }
}