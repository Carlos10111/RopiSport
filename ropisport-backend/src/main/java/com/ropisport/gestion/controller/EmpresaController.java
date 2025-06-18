package com.ropisport.gestion.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
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

import com.ropisport.gestion.model.dto.request.EmpresaRequest;
import com.ropisport.gestion.model.dto.request.SearchRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.EmpresaResponse;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;
import com.ropisport.gestion.service.EmpresaService;
import com.ropisport.gestion.service.ExcelExportService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<EmpresaResponse>> getAllEmpresas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<EmpresaResponse> empresas = empresaService.getAllEmpresas(pageable);

        PaginatedResponse<EmpresaResponse> response = new PaginatedResponse<>(
            empresas.getContent(),
            empresas.getNumber(),
            empresas.getSize(),
            empresas.getTotalElements(),
            empresas.getTotalPages(),
            empresas.isLast()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponse> getEmpresaById(@PathVariable Integer id) {
        EmpresaResponse empresa = empresaService.getEmpresaById(id);
        return ResponseEntity.ok(empresa);
    }

    // ✅ CAMBIAR ESTE ENDPOINT - Ahora devuelve una lista
    @GetMapping("/socia/{sociaId}")
    public ResponseEntity<List<EmpresaResponse>> getEmpresasBySociaId(@PathVariable Integer sociaId) {
        List<EmpresaResponse> empresas = empresaService.getEmpresasBySociaId(sociaId);
        return ResponseEntity.ok(empresas);
    }

    @PostMapping
    public ResponseEntity<EmpresaResponse> createEmpresa(@Valid @RequestBody EmpresaRequest empresaRequest) {
        EmpresaResponse nuevaEmpresa = empresaService.createEmpresa(empresaRequest);
        return new ResponseEntity<>(nuevaEmpresa, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponse> updateEmpresa(
            @PathVariable Integer id,
            @Valid @RequestBody EmpresaRequest empresaRequest) {
        EmpresaResponse empresaActualizada = empresaService.updateEmpresa(id, empresaRequest);
        return ResponseEntity.ok(empresaActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmpresa(@PathVariable Integer id) {
        empresaService.deleteEmpresa(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Empresa eliminada correctamente"));
    }

    @PostMapping("/search")
    public ResponseEntity<List<EmpresaResponse>> searchEmpresas(@RequestBody SearchRequest searchRequest) {
        List<EmpresaResponse> empresas = empresaService.searchEmpresas(searchRequest.getSearchTerm());
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=empresas.xlsx");

        excelExportService.exportEmpresasToExcel(response.getOutputStream());
    }

    // Búsqueda general
    @GetMapping("/buscar")
    public ResponseEntity<PaginatedResponse<EmpresaResponse>> busquedaGeneral(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) Boolean activa,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        PaginatedResponse<EmpresaResponse> response = empresaService.busquedaGeneral(
                texto, activa, page, size, sort);

        return ResponseEntity.ok(response);
    }

    // Búsqueda avanzada
    @GetMapping("/busqueda-avanzada")
    public ResponseEntity<PaginatedResponse<EmpresaResponse>> busquedaAvanzada(
            @RequestParam(required = false) String nombreNegocio,
            @RequestParam(required = false) String cif,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String telefono,
            @RequestParam(required = false) String direccion,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Boolean activa,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort) {

        PaginatedResponse<EmpresaResponse> response = empresaService.busquedaAvanzada(
                nombreNegocio, cif, email, telefono, direccion, categoriaId, activa, page, size, sort);

        return ResponseEntity.ok(response);
    }
}