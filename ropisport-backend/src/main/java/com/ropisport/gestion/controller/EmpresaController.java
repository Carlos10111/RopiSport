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

    @GetMapping("/socia/{sociaId}")
    public ResponseEntity<EmpresaResponse> getEmpresaBySociaId(@PathVariable Integer sociaId) {
        EmpresaResponse empresa = empresaService.getEmpresaBySociaId(sociaId);
        return ResponseEntity.ok(empresa);
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
}