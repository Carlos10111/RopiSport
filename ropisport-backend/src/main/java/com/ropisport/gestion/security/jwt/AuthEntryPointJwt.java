package com.ropisport.gestion.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ropisport.gestion.exception.ApiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Punto de entrada para manejar errores de autenticación
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        logger.error("Error de autenticación: {}", authException.getMessage());
        
        ApiError apiError = new ApiError();
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setStatus(HttpStatus.UNAUTHORIZED.value());
        apiError.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        apiError.setMessage("No está autorizado para acceder a este recurso");
        apiError.setPath(request.getRequestURI());
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        objectMapper.findAndRegisterModules(); // Para manejar LocalDateTime
        objectMapper.writeValue(response.getOutputStream(), apiError);
    }
}