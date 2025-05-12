package com.ropisport.gestion.controller;

import com.ropisport.gestion.model.dto.request.RolRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.RolResponse;
import com.ropisport.gestion.service.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
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
        RolResponse nuevoRol = rolService.createRol(rolRequest);
        return new ResponseEntity<>(nuevoRol, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RolResponse> updateRol(
            @PathVariable Integer id,
            @Valid @RequestBody RolRequest rolRequest) {
        RolResponse rolActualizado = rolService.updateRol(id, rolRequest);
        return ResponseEntity.ok(rolActualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteRol(@PathVariable Integer id) {
        rolService.deleteRol(id);
        return ResponseEntity.ok(new ApiResponse(true, "Rol eliminado correctamente"));
    }
}