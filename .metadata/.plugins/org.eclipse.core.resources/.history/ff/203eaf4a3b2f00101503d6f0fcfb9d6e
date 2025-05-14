package com.ropisport.gestion.controller;

import com.ropisport.gestion.exception.ValidationException;
import com.ropisport.gestion.model.dto.request.LoginRequest;
import com.ropisport.gestion.model.dto.request.PasswordChangeRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.JwtResponse;
import com.ropisport.gestion.security.jwt.JwtUtils;
import com.ropisport.gestion.security.jwt.UserDetailsImpl;
import com.ropisport.gestion.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controlador para la autenticación de usuarios
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private UsuarioService usuarioService;
    
    /**
     * Autentica un usuario y genera un token JWT
     * @param loginRequest datos de inicio de sesión
     * @return respuesta con el token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            
            return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getRol()
            ));
        } catch (BadCredentialsException e) {
            throw new ValidationException("Credenciales incorrectas");
        }
    }
    
    /**
     * Cambia la contraseña del usuario autenticado
     * @param request datos de cambio de contraseña
     * @return respuesta con mensaje de éxito
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordChangeRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        ApiResponse response = usuarioService.changePassword(userDetails.getId(), request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Verifica si un token JWT es válido
     * @param token token JWT a verificar
     * @return respuesta con mensaje de éxito o error
     */
    @GetMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        boolean isValid = jwtUtils.validateJwtToken(token);
        if (isValid) {
            return ResponseEntity.ok(new ApiResponse(true, "Token válido"));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Token inválido o expirado"));
        }
    }
    
    /**
     * Obtiene información del usuario autenticado
     * @return información del usuario
     */
    @GetMapping("/user-info")
    public ResponseEntity<?> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        return ResponseEntity.ok(new JwtResponse(
            null, // No enviamos el token de nuevo
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            userDetails.getRol()
        ));
    }
}