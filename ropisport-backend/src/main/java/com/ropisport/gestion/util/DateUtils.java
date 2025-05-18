package com.ropisport.gestion.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilidades para manejo de fechas
 */
public final class DateUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT);

    private DateUtils() {
        // Constructor privado para evitar instanciación
    }

    /**
     * Convierte una fecha a string en formato dd/MM/yyyy
     * @param date fecha a convertir
     * @return string con la fecha formateada
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }

    /**
     * Convierte una fecha y hora a string en formato dd/MM/yyyy HH:mm:ss
     * @param dateTime fecha y hora a convertir
     * @return string con la fecha y hora formateada
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATETIME_FORMATTER);
    }

    /**
     * Convierte un string en formato dd/MM/yyyy a LocalDate
     * @param dateStr string con la fecha
     * @return objeto LocalDate
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    /**
     * Convierte un string en formato dd/MM/yyyy HH:mm:ss a LocalDateTime
     * @param dateTimeStr string con la fecha y hora
     * @return objeto LocalDateTime
     */
    public static LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
    }

    /**
     * Obtiene la fecha y hora actual
     * @return fecha y hora actual
     */
    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}