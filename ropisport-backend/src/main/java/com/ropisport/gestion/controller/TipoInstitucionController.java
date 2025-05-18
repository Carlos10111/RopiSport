package com.ropisport.gestion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ropisport.gestion.model.dto.request.TipoInstitucionRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.TipoInstitucionResponse;
import com.ropisport.gestion.service.TipoInstitucionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tipos-institucion")
public class TipoInstitucionController {

    @Autowired
    private TipoInstitucionService tipoInstitucionService;

    @GetMapping
    public ResponseEntity<List<TipoInstitucionResponse>> getAllTiposInstitucion() {
        List<TipoInstitucionResponse> tipos = tipoInstitucionService.getAllTiposInstitucion();
        return ResponseEntity.ok(tipos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoInstitucionResponse> getTipoInstitucionById(@PathVariable Integer id) {
        TipoInstitucionResponse tipo = tipoInstitucionService.getTipoInstitucionById(id);
        return ResponseEntity.ok(tipo);
    }

    @PostMapping
    public ResponseEntity<TipoInstitucionResponse> createTipoInstitucion(@Valid @RequestBody TipoInstitucionRequest tipoRequest) {
        TipoInstitucionResponse nuevoTipo = tipoInstitucionService.createTipoInstitucion(tipoRequest);
        return new ResponseEntity<>(nuevoTipo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoInstitucionResponse> updateTipoInstitucion(
            @PathVariable Integer id,
            @Valid @RequestBody TipoInstitucionRequest tipoRequest) {
        TipoInstitucionResponse tipoActualizado = tipoInstitucionService.updateTipoInstitucion(id, tipoRequest);
        return ResponseEntity.ok(tipoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTipoInstitucion(@PathVariable Integer id) {
        tipoInstitucionService.deleteTipoInstitucion(id);
        return ResponseEntity.ok(new ApiResponse<Void>(true, "Tipo de institución eliminado correctamente"));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TipoInstitucionResponse>> searchTiposInstitucion(@RequestParam String nombre) {
        List<TipoInstitucionResponse> tipos = tipoInstitucionService.searchTiposByNombre(nombre);
        return ResponseEntity.ok(tipos);
    }
}