package com.ropisport.gestion.controller;

import com.ropisport.gestion.model.dto.request.CategoriaRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.CategoriaResponse;
import com.ropisport.gestion.service.CategoriaNegocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {
    
    @Autowired
    private CategoriaNegocioService categoriaService;
    
    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> getAllCategorias() {
        List<CategoriaResponse> categorias = categoriaService.getAllCategorias();
        return ResponseEntity.ok(categorias);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> getCategoriaById(@PathVariable Integer id) {
        CategoriaResponse categoria = categoriaService.getCategoriaById(id);
        return ResponseEntity.ok(categoria);
    }
    
    @PostMapping
    public ResponseEntity<CategoriaResponse> createCategoria(@Valid @RequestBody CategoriaRequest categoriaRequest) {
        CategoriaResponse nuevaCategoria = categoriaService.createCategoria(categoriaRequest);
        return new ResponseEntity<>(nuevaCategoria, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> updateCategoria(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaRequest categoriaRequest) {
        CategoriaResponse categoriaActualizada = categoriaService.updateCategoria(id, categoriaRequest);
        return ResponseEntity.ok(categoriaActualizada);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategoria(@PathVariable Integer id) {
        categoriaService.deleteCategoria(id);
        return ResponseEntity.ok(new ApiResponse(true, "Categoría eliminada correctamente"));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<CategoriaResponse>> searchCategorias(@RequestParam String nombre) {
        List<CategoriaResponse> categorias = categoriaService.searchCategoriasByNombre(nombre);
        return ResponseEntity.ok(categorias);
    }
}