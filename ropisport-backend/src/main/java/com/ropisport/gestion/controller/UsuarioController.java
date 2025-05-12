package com.ropisport.gestion.controller;

import com.ropisport.gestion.model.dto.request.UsuarioRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;
import com.ropisport.gestion.model.dto.response.UsuarioResponse;
import com.ropisport.gestion.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaginatedResponse<UsuarioResponse>> getAllUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<UsuarioResponse> usuarios = usuarioService.getAllUsuarios(pageable);
        
        PaginatedResponse<UsuarioResponse> response = new PaginatedResponse<>(
            usuarios.getContent(),
            usuarios.getNumber(),
            usuarios.getSize(),
            usuarios.getTotalElements(),
            usuarios.getTotalPages(),
            usuarios.isLast()
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UsuarioResponse> getUsuarioById(@PathVariable Integer id) {
        UsuarioResponse usuario = usuarioService.getUsuarioById(id);
        return ResponseEntity.ok(usuario);
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> createUsuario(@Valid @RequestBody UsuarioRequest usuarioRequest) {
        UsuarioResponse nuevoUsuario = usuarioService.createUsuario(usuarioRequest);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UsuarioResponse> updateUsuario(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioRequest usuarioRequest) {
        UsuarioResponse usuarioActualizado = usuarioService.updateUsuario(id, usuarioRequest);
        return ResponseEntity.ok(usuarioActualizado);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.ok(new ApiResponse(true, "Usuario eliminado correctamente"));
    }
    
    @GetMapping("/rol/{rolId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioResponse>> getUsuariosByRol(@PathVariable Integer rolId) {
        List<UsuarioResponse> usuarios = usuarioService.getUsuariosByRol(rolId);
        return ResponseEntity.ok(usuarios);
    }
}