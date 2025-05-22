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

        PaginatedResponse<SociaResponse> response = sociaService.getAllSocias(page, size, sort);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SociaResponse> getSociaById(@PathVariable Integer id) {
        SociaResponse response = sociaService.getSociaById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<SociaResponse> createSocia(@Valid @RequestBody SociaRequest request) {
        SociaResponse response = sociaService.createSocia(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<SociaResponse> updateSocia(
            @PathVariable Integer id,
            @Valid @RequestBody SociaRequest request) {

        SociaResponse response = sociaService.updateSocia(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSocia(@PathVariable Integer id) {
        sociaService.deleteSocia(id);
        return ResponseEntity.ok(new ApiResponse<Void>(true, "Socia eliminada correctamente"));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<SociaResponse> cambiarEstado(
            @PathVariable Integer id,
            @RequestParam Boolean activa,
            @RequestParam(required = false) String observaciones) {

        SociaResponse response = sociaService.cambiarEstado(id, activa, observaciones);
        return ResponseEntity.ok(response);
    }

    // Búsqueda general (simple)
    @GetMapping("/buscar")
    public ResponseEntity<PaginatedResponse<SociaResponse>> busquedaGeneral(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) Boolean activa,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        PaginatedResponse<SociaResponse> response = sociaService.busquedaGeneral(
                texto, activa, page, size, sort);

        return ResponseEntity.ok(response);
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

        PaginatedResponse<SociaResponse> response = sociaService.busquedaAvanzada(
                nombre, nombreNegocio, email, telefono, cif, categoriaId, activa, page, size, sort);

        return ResponseEntity.ok(response);
    }

    // Exportación a Excel
    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN') or hasRole('GESTOR')")
    public ResponseEntity<byte[]> exportToExcel(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String nombreNegocio,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String cif,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) String estado) {

        Map<String, String> parametros = new HashMap<>();
        if (texto != null) {
			parametros.put("texto", texto);
		}
        if (nombre != null) {
			parametros.put("nombre", nombre);
		}
        if (nombreNegocio != null) {
			parametros.put("nombreNegocio", nombreNegocio);
		}
        if (email != null) {
			parametros.put("email", email);
		}
        if (telefono != null) {
			parametros.put("telefono", telefono);
		}
        if (cif != null) {
			parametros.put("cif", cif);
		}
        if (categoriaId != null) {
			parametros.put("categoriaId", categoriaId.toString());
		}
        if (estado != null) {
			parametros.put("estado", estado);
		}

        byte[] excelFile = sociaService.exportToExcel(parametros);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "socias.xlsx");

        return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
    }
}