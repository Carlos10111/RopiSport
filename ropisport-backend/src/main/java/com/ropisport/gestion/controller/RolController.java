package com.ropisport.gestion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ropisport.gestion.exception.ValidationException;
import com.ropisport.gestion.model.dto.request.RolRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.RolResponse;
import com.ropisport.gestion.service.RolService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/roles")
@Slf4j
public class RolController {
    
    @Autowired
    private RolService rolService;

    @GetMapping
    public ResponseEntity<List<RolResponse>> getAllRoles() {
        List<RolResponse> roles = rolService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolResponse> getRolById(@PathVariable Integer id) {
        RolResponse rol = rolService.getRolById(id);
        return ResponseEntity.ok(rol);
    }

    @PostMapping
    public ResponseEntity<RolResponse> createRol(@Valid @RequestBody RolRequest rolRequest) {
        // Validación básica de seguridad
        validateBasicSecurity(rolRequest.getNombre());
        
        String currentUser = getCurrentUser();
        log.info("User {} creating role: {}", currentUser, rolRequest.getNombre());
        
        RolResponse nuevoRol = rolService.createRol(rolRequest);
        return new ResponseEntity<>(nuevoRol, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolResponse> updateRol(
            @PathVariable Integer id,
            @Valid @RequestBody RolRequest rolRequest) {
        
        // Validación básica de seguridad
        validateBasicSecurity(rolRequest.getNombre());
        
        String currentUser = getCurrentUser();
        log.info("User {} updating role ID: {}", currentUser, id);
        
        RolResponse rolActualizado = rolService.updateRol(id, rolRequest);
        return ResponseEntity.ok(rolActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRol(@PathVariable Integer id) {
        String currentUser = getCurrentUser();
        log.warn("User {} deleting role ID: {}", currentUser, id);
        
        rolService.deleteRol(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Rol eliminado correctamente"));
    }
    
    // Métodos auxiliares para seguridad
    private void validateBasicSecurity(String nombre) {
        if (nombre != null && (nombre.contains("<") || nombre.contains(">") || 
            nombre.contains("script") || nombre.length() > 100)) {
            throw new ValidationException("Nombre de rol inválido");
        }
    }
    
    private String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}