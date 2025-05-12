package com.ropisport.gestion.controller;

import com.ropisport.gestion.model.dto.request.InstitucionRequest;
import com.ropisport.gestion.model.dto.request.SearchRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.InstitucionResponse;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;
import com.ropisport.gestion.service.InstitucionService;
import com.ropisport.gestion.service.ExcelExportService;
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
@RequestMapping("/api/instituciones")
public class InstitucionController {
    
    @Autowired
    private InstitucionService institucionService;
    
    @Autowired
    private ExcelExportService excelExportService;
    
    @GetMapping
    public ResponseEntity<PaginatedResponse<InstitucionResponse>> getAllInstituciones(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<InstitucionResponse> instituciones = institucionService.getAllInstituciones(pageable);
        
        PaginatedResponse<InstitucionResponse> response = new PaginatedResponse<>(
            instituciones.getContent(),
            instituciones.getNumber(),
            instituciones.getSize(),
            instituciones.getTotalElements(),
            instituciones.getTotalPages(),
            instituciones.isLast()
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InstitucionResponse> getInstitucionById(@PathVariable Integer id) {
        InstitucionResponse institucion = institucionService.getInstitucionById(id);
        return ResponseEntity.ok(institucion);
    }
    
    @GetMapping("/tipo/{tipoId}")
    public ResponseEntity<List<InstitucionResponse>> getInstitucionesByTipoId(@PathVariable Integer tipoId) {
        List<InstitucionResponse> instituciones = institucionService.getInstitucionesByTipoId(tipoId);
        return ResponseEntity.ok(instituciones);
    }
    
    @PostMapping
    public ResponseEntity<InstitucionResponse> createInstitucion(@Valid @RequestBody InstitucionRequest institucionRequest) {
        InstitucionResponse nuevaInstitucion = institucionService.createInstitucion(institucionRequest);
        return new ResponseEntity<>(nuevaInstitucion, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<InstitucionResponse> updateInstitucion(
            @PathVariable Integer id,
            @Valid @RequestBody InstitucionRequest institucionRequest) {
        InstitucionResponse institucionActualizada = institucionService.updateInstitucion(id, institucionRequest);
        return ResponseEntity.ok(institucionActualizada);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteInstitucion(@PathVariable Integer id) {
        institucionService.deleteInstitucion(id);
        return ResponseEntity.ok(new ApiResponse(true, "Institución eliminada correctamente"));
    }
    
    @PostMapping("/search")
    public ResponseEntity<List<InstitucionResponse>> searchInstituciones(@RequestBody SearchRequest searchRequest) {
        List<InstitucionResponse> instituciones = institucionService.searchInstituciones(searchRequest.getSearchTerm());
        return ResponseEntity.ok(instituciones);
    }
    
    @GetMapping("/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=instituciones.xlsx");
        
        excelExportService.exportInstitucionesToExcel(response.getOutputStream());
    }
}