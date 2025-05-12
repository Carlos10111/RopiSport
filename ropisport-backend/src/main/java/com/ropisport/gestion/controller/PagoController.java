package com.ropisport.gestion.controller;

import com.ropisport.gestion.model.dto.request.PagoRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.PagoResponse;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;
import com.ropisport.gestion.service.PagoService;
import com.ropisport.gestion.service.ExcelExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {
    
    @Autowired
    private PagoService pagoService;
    
    @Autowired
    private ExcelExportService excelExportService;
    
    @GetMapping
    public ResponseEntity<PaginatedResponse<PagoResponse>> getAllPagos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fechaPago") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<PagoResponse> pagos = pagoService.getAllPagos(pageable);
        
        PaginatedResponse<PagoResponse> response = new PaginatedResponse<>(
            pagos.getContent(),
            pagos.getNumber(),
            pagos.getSize(),
            pagos.getTotalElements(),
            pagos.getTotalPages(),
            pagos.isLast()
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> getPagoById(@PathVariable Integer id) {
        PagoResponse pago = pagoService.getPagoById(id);
        return ResponseEntity.ok(pago);
    }
    
    @GetMapping("/socia/{sociaId}")
    public ResponseEntity<List<PagoResponse>> getPagosBySociaId(@PathVariable Integer sociaId) {
        List<PagoResponse> pagos = pagoService.getPagosBySociaId(sociaId);
        return ResponseEntity.ok(pagos);
    }
    
    @GetMapping("/fecha")
    public ResponseEntity<List<PagoResponse>> getPagosByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        List<PagoResponse> pagos = pagoService.getPagosByFechaBetween(inicio, fin);
        return ResponseEntity.ok(pagos);
    }
    
    @GetMapping("/confirmados")
    public ResponseEntity<List<PagoResponse>> getPagosConfirmados(@RequestParam Boolean confirmado) {
        List<PagoResponse> pagos = pagoService.getPagosByConfirmado(confirmado);
        return ResponseEntity.ok(pagos);
    }
    
    @PostMapping
    public ResponseEntity<PagoResponse> createPago(@Valid @RequestBody PagoRequest pagoRequest) {
        PagoResponse nuevoPago = pagoService.createPago(pagoRequest);
        return new ResponseEntity<>(nuevoPago, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PagoResponse> updatePago(
            @PathVariable Integer id,
            @Valid @RequestBody PagoRequest pagoRequest) {
        PagoResponse pagoActualizado = pagoService.updatePago(id, pagoRequest);
        return ResponseEntity.ok(pagoActualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deletePago(@PathVariable Integer id) {
        pagoService.deletePago(id);
        return ResponseEntity.ok(new ApiResponse(true, "Pago eliminado correctamente"));
    }
    
    @GetMapping("/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pagos.xlsx");
        
        excelExportService.exportPagosToExcel(response.getOutputStream());
    }
}