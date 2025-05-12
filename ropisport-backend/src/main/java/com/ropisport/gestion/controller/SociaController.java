package com.ropisport.gestion.controller;

import com.ropisport.gestion.model.dto.request.SearchRequest;
import com.ropisport.gestion.model.dto.request.SociaRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;
import com.ropisport.gestion.model.dto.response.SociaResponse;
import com.ropisport.gestion.service.ExcelExportService;
import com.ropisport.gestion.service.SociaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/socias")
public class SociaController {
    
    @Autowired
    private SociaService sociaService;
    
    @Autowired
    private ExcelExportService excelExportService;
    
    @GetMapping
    public ResponseEntity<PaginatedResponse<SociaResponse>> getAllSocias(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "numeroSocia") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<SociaResponse> socias = sociaService.getAllSocias(pageable);
        
        PaginatedResponse<SociaResponse> response = new PaginatedResponse<>(
            socias.getContent(),
            socias.getNumber(),
            socias.getSize(),
            socias.getTotalElements(),
            socias.getTotalPages(),
            socias.isLast()
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SociaResponse> getSociaById(@PathVariable Integer id) {
        SociaResponse socia = sociaService.getSociaById(id);
        return ResponseEntity.ok(socia);
    }
    
    @GetMapping("/numero/{numeroSocia}")
    public ResponseEntity<SociaResponse> getSociaByNumero(@PathVariable String numeroSocia) {
        SociaResponse socia = sociaService.getSociaByNumero(numeroSocia);
        return ResponseEntity.ok(socia);
    }
    
    @GetMapping("/activas")
    public ResponseEntity<List<SociaResponse>> getSociasActivas() {
        List<SociaResponse> socias = sociaService.getSociasByActiva(true);
        return ResponseEntity.ok(socias);
    }
    
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<SociaResponse>> getSociasByCategoria(@PathVariable Integer categoriaId) {
        List<SociaResponse> socias = sociaService.getSociasByCategoria(categoriaId);
        return ResponseEntity.ok(socias);
    }
    
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<SociaResponse> getSociaByUsuarioId(@PathVariable Integer usuarioId) {
        SociaResponse socia = sociaService.getSociaByUsuarioId(usuarioId);
        return ResponseEntity.ok(socia);
    }
    
    @PostMapping
    public ResponseEntity<SociaResponse> createSocia(@Valid @RequestBody SociaRequest sociaRequest) {
        SociaResponse nuevaSocia = sociaService.createSocia(sociaRequest);
        return new ResponseEntity<>(nuevaSocia, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SociaResponse> updateSocia(
            @PathVariable Integer id,
            @Valid @RequestBody SociaRequest sociaRequest) {
        SociaResponse sociaActualizada = sociaService.updateSocia(id, sociaRequest);
        return ResponseEntity.ok(sociaActualizada);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSocia(@PathVariable Integer id) {
        sociaService.deleteSocia(id);
        return ResponseEntity.ok(new ApiResponse(true, "Socia eliminada correctamente"));
    }
    
    @PostMapping("/search")
    public ResponseEntity<List<SociaResponse>> searchSocias(@RequestBody SearchRequest searchRequest) {
        List<SociaResponse> socias = sociaService.searchSocias(searchRequest.getSearchTerm());
        return ResponseEntity.ok(socias);
    }
    
    @GetMapping("/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=socias.xlsx");
        
        excelExportService.exportSociasToExcel(response.getOutputStream());
    }
}