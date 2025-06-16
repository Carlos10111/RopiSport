package com.ropisport.gestion.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ropisport.gestion.model.dto.request.PasswordChangeRequest;
import com.ropisport.gestion.model.dto.request.UsuarioRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.UsuarioResponse;
import com.ropisport.gestion.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@PreAuthorize("hasRole('ADMIN')")  // el administrador general puede acceder a este controlador
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;



    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getUsuarioById(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.getUsuarioById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UsuarioResponse> getUsuarioByUsername(@PathVariable String username) {
        return ResponseEntity.ok(usuarioService.getUsuarioByUsername(username));
    }

    @GetMapping("/rol/{rolId}")
    public ResponseEntity<List<UsuarioResponse>> getUsuariosByRol(@PathVariable Integer rolId) {
        return ResponseEntity.ok(usuarioService.getUsuariosByRol(rolId));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> createUsuario(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(usuarioService.createUsuario(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> updateUsuario(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.updateUsuario(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable Integer id,
            @Valid @RequestBody PasswordChangeRequest request) {
        return ResponseEntity.ok(usuarioService.changePassword(id, request));
    }


    // Endpoints  para crear administradores

    @PostMapping("/admin-general")
    public ResponseEntity<ApiResponse<UsuarioResponse>> crearAdministradorGeneral(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse usuario = usuarioService.crearAdministradorGeneral(request);
        ApiResponse<UsuarioResponse> response = new ApiResponse<>(true, "Administrador general creado exitosamente", usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/admin-socias")
    public ResponseEntity<ApiResponse<UsuarioResponse>> crearAdministradorSocias(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse usuario = usuarioService.crearAdministradorSocias(request);
        ApiResponse<UsuarioResponse> response = new ApiResponse<>(true, "Administrador de socias creado exitosamente", usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/buscar")
    public ResponseEntity<PaginatedResponse<UsuarioResponse>> busquedaGeneral(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) Integer rolId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        PaginatedResponse<UsuarioResponse> response = usuarioService.busquedaGeneral(
                texto, activo, rolId, page, size, sort);

        return ResponseEntity.ok(response);
    }

    // Búsqueda avanzada
    @GetMapping("/busqueda-avanzada")
    public ResponseEntity<PaginatedResponse<UsuarioResponse>> busquedaAvanzada(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String nombreCompleto,
            @RequestParam(required = false) Integer rolId,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        PaginatedResponse<UsuarioResponse> response = usuarioService.busquedaAvanzada(
                username, email, nombreCompleto, rolId, activo, page, size, sort);

        return ResponseEntity.ok(response);
    }

    @GetMapping
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
}