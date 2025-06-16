package com.ropisport.gestion.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ropisport.gestion.model.dto.request.SociaRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;
import com.ropisport.gestion.model.dto.response.SociaResponse;
import com.ropisport.gestion.service.SociaService;
import com.ropisport.gestion.util.Constants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/socias")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SociaController {

    @Autowired
    private SociaService sociaService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<SociaResponse>> getAllSocias(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {
        
        try {
            System.out.println("🔄 GET /api/socias - Parámetros: page=" + page + ", size=" + size + ", sort=" + sort);
            PaginatedResponse<SociaResponse> response = sociaService.getAllSocias(page, size, sort);
            System.out.println("✅ Respuesta exitosa con " + response.getContent().size() + " elementos");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error en getAllSocias: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SociaResponse> getSociaById(@PathVariable Integer id) {
        try {
            System.out.println("🔄 GET /api/socias/" + id);
            SociaResponse response = sociaService.getSociaById(id);
            System.out.println("✅ Socia encontrada: " + response.getNombre());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error en getSociaById: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "') or hasRole('" + Constants.ROLE_ADMIN_SOCIAS + "')")
    public ResponseEntity<SociaResponse> createSocia(@Valid @RequestBody SociaRequest request) {
        try {
            System.out.println("🔄 POST /api/socias - Creando socia:");
            System.out.println("Nombre: " + request.getNombre());
            System.out.println("Email: " + request.getEmail());
            System.out.println("NumeroSocia: " + request.getNumeroSocia());
            System.out.println("FechaInicio: " + request.getFechaInicio());
            System.out.println("CategoriaId: " + request.getCategoriaId());
            System.out.println("Activa: " + request.getActiva());
            
            SociaResponse response = sociaService.createSocia(request);
            System.out.println("✅ Socia creada exitosamente con ID: " + response.getId());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            System.err.println("❌ Error al crear socia: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "') or hasRole('" + Constants.ROLE_ADMIN_SOCIAS + "')")
    public ResponseEntity<SociaResponse> updateSocia(
            @PathVariable Integer id,
            @Valid @RequestBody SociaRequest request) {
        
        try {
            System.out.println("🔄 PUT /api/socias/" + id);
            SociaResponse response = sociaService.updateSocia(id, request);
            System.out.println("✅ Socia actualizada exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error al actualizar socia: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "')")
    public ResponseEntity<ApiResponse<Void>> deleteSocia(@PathVariable Integer id) {
        try {
            System.out.println("🔄 DELETE /api/socias/" + id);
            sociaService.deleteSocia(id);
            System.out.println("✅ Socia eliminada exitosamente");
            return ResponseEntity.ok(new ApiResponse<Void>(true, "Socia eliminada correctamente"));
        } catch (Exception e) {
            System.err.println("❌ Error al eliminar socia: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('" + Constants.ROLE_ADMIN + "') or hasRole('" + Constants.ROLE_ADMIN_SOCIAS + "')")
    public ResponseEntity<SociaResponse> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam Boolean activa,
            @RequestParam(required = false) String observaciones) {
        
        try {
            System.out.println("🔄 PATCH /api/socias/" + id + "/estado - Activa: " + activa);
            SociaResponse response = sociaService.cambiarEstado(id, activa, observaciones);
            System.out.println("✅ Estado cambiado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error al cambiar estado: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Búsqueda general (simple)
    @GetMapping("/buscar")
    public ResponseEntity<PaginatedResponse<SociaResponse>> busquedaGeneral(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) Boolean activa,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        try {
            System.out.println("🔄 GET /api/socias/buscar - Texto: " + texto + ", Activa: " + activa);
            PaginatedResponse<SociaResponse> response = sociaService.busquedaGeneral(
                    texto, activa, page, size, sort);
            System.out.println("✅ Búsqueda exitosa con " + response.getContent().size() + " resultados");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error en búsqueda general: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Búsqueda avanzada
    @GetMapping("/busqueda-avanzada")
    public ResponseEntity<PaginatedResponse<SociaResponse>> busquedaAvanzada(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String nombreNegocio,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String cif,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Boolean activa,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        try {
            System.out.println("🔄 GET /api/socias/busqueda-avanzada");
            PaginatedResponse<SociaResponse> response = sociaService.busquedaAvanzada(
                    nombre, nombreNegocio, email, telefono, cif, categoriaId, activa, page, size, sort);
            System.out.println("✅ Búsqueda avanzada exitosa con " + response.getContent().size() + " resultados");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error en búsqueda avanzada: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}