package com.ropisport.gestion.util;

/**
 * Constantes utilizadas en la aplicación
 */
public final class Constants {

    // Roles del sistema
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    public static final String ROLE_SOCIA = "SOCIA";

    // Prefijos para autorización
    public static final String ROLE_PREFIX = "ROLE_";

    // JWT
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";

    // Paginación por defecto
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;

    // Formatos de fecha
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    private Constants() {
        // Constructor privado para evitar instanciación
    }
}