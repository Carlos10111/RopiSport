package com.ropisport.gestion.util;

import java.util.regex.Pattern;

/**
 * Utilidades para validación de datos
 */
public final class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    private static final Pattern CIF_PATTERN = Pattern.compile(
            "^[a-zA-Z][0-9]{7}[a-zA-Z0-9]$");

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[0-9\\s]{9,15}$");

    private ValidationUtils() {
        // Constructor privado para evitar instanciación
    }

    /**
     * Valida si un email tiene formato correcto
     * @param email email a validar
     * @return true si el formato es válido, false en caso contrario
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Valida si un CIF tiene formato correcto
     * @param cif CIF a validar
     * @return true si el formato es válido, false en caso contrario
     */
    public static boolean isValidCif(String cif) {
        if (cif == null || cif.isEmpty()) {
            return false;
        }
        return CIF_PATTERN.matcher(cif).matches();
    }

    /**
     * Valida si un teléfono tiene formato correcto
     * @param phone teléfono a validar
     * @return true si el formato es válido, false en caso contrario
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Verifica si un string es nulo o está vacío
     * @param str string a verificar
     * @return true si es nulo o vacío, false en caso contrario
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}