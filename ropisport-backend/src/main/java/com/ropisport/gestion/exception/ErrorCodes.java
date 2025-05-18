package com.ropisport.gestion.exception;

/**
 * Códigos de error para la aplicación
 */
public final class ErrorCodes {

    // Códigos generales
    public static final String VALIDATION_ERROR = "ERROR-01";
    public static final String ENTITY_NOT_FOUND = "ERROR-02";
    public static final String RESOURCE_ALREADY_EXISTS = "ERROR-03";
    public static final String UNAUTHORIZED = "ERROR-04";
    public static final String FORBIDDEN = "ERROR-05";
    public static final String BAD_REQUEST = "ERROR-06";
    public static final String INTERNAL_SERVER_ERROR = "ERROR-07";

    // Códigos específicos
    public static final String INVALID_CREDENTIALS = "ERROR-10";
    public static final String INVALID_TOKEN = "ERROR-11";
    public static final String EXPIRED_TOKEN = "ERROR-12";

    private ErrorCodes() {
        // Constructor privado para evitar instanciación
    }
}