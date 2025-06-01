package com.ropisport.gestion.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
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

import com.ropisport.gestion.model.dto.request.PagoRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;
import com.ropisport.gestion.model.dto.response.PagoResponse;
import com.ropisport.gestion.service.ExcelExportService;
import com.ropisport.gestion.service.PagoService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

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
    public ResponseEntity<ApiResponse<Void>> deletePago(@PathVariable Integer id) {
        pagoService.deletePago(id);
        return ResponseEntity.ok(new ApiResponse<Void>(true, "Pago eliminado correctamente"));
    }

    @GetMapping("/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pagos.xlsx");

        excelExportService.exportPagosToExcel(response.getOutputStream());
    }

 // Búsqueda general
 @GetMapping("/buscar")
 public ResponseEntity<PaginatedResponse<PagoResponse>> busquedaGeneral(
         @RequestParam(required = false) String texto,
         @RequestParam(required = false) Boolean confirmado,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(defaultValue = "fechaPago,desc") String sort) {

     PaginatedResponse<PagoResponse> response = pagoService.busquedaGeneral(
             texto, confirmado, page, size, sort);

     return ResponseEntity.ok(response);
 }

 // Búsqueda avanzada
 @GetMapping("/busqueda-avanzada")
 public ResponseEntity<PaginatedResponse<PagoResponse>> busquedaAvanzada(
         @RequestParam(required = false) String concepto,
         @RequestParam(required = false) String referencia,
         @RequestParam(required = false) Integer sociaId,
         @RequestParam(required = false) BigDecimal importeMin,
         @RequestParam(required = false) BigDecimal importeMax,
         @RequestParam(required = false) Boolean confirmado,
         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(defaultValue = "fechaPago,desc") String sort) {

     PaginatedResponse<PagoResponse> response = pagoService.busquedaAvanzada(
             concepto, referencia, sociaId, importeMin, importeMax, confirmado, 
             fechaInicio, fechaFin, page, size, sort);

     return ResponseEntity.ok(response);
 }
}