package com.ropisport.gestion.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import org.springframework.web.bind.annotation.RestController;

import com.ropisport.gestion.model.dto.request.PagoDetalleRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.PagoDetalleResponse;
import com.ropisport.gestion.service.PagoDetalleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/pagos-detalle")
public class PagoDetalleController {

    @Autowired
    private PagoDetalleService pagoDetalleService;

    @GetMapping("/pago/{pagoId}")
    public ResponseEntity<List<PagoDetalleResponse>> getDetallesByPagoId(@PathVariable Integer pagoId) {
        List<PagoDetalleResponse> detalles = pagoDetalleService.getDetallesByPagoId(pagoId);
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDetalleResponse> getDetalleById(@PathVariable Integer id) {
        PagoDetalleResponse detalle = pagoDetalleService.getDetalleById(id);
        return ResponseEntity.ok(detalle);
    }

    @PostMapping
    public ResponseEntity<PagoDetalleResponse> createDetalle(@Valid @RequestBody PagoDetalleRequest detalleRequest) {
        PagoDetalleResponse nuevoDetalle = pagoDetalleService.createDetalle(detalleRequest);
        return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagoDetalleResponse> updateDetalle(
            @PathVariable Integer id,
            @Valid @RequestBody PagoDetalleRequest detalleRequest) {
        PagoDetalleResponse detalleActualizado = pagoDetalleService.updateDetalle(id, detalleRequest);
        return ResponseEntity.ok(detalleActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDetalle(@PathVariable Integer id) {
        pagoDetalleService.deleteDetalle(id);
        return ResponseEntity.ok(new ApiResponse<Void>(true, "Detalle de pago eliminado correctamente"));
    }


 // Búsqueda general
 @GetMapping("/buscar")
 public ResponseEntity<PaginatedResponse<PagoDetalleResponse>> busquedaGeneral(
         @RequestParam(required = false) String texto,
         @RequestParam(required = false) Integer pagoId,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(defaultValue = "fechaDetalle,desc") String sort) {

     PaginatedResponse<PagoDetalleResponse> response = pagoDetalleService.busquedaGeneral(
             texto, pagoId, page, size, sort);

     return ResponseEntity.ok(response);
 }

 // Búsqueda avanzada
 @GetMapping("/busqueda-avanzada")
 public ResponseEntity<PaginatedResponse<PagoDetalleResponse>> busquedaAvanzada(
         @RequestParam(required = false) String concepto,
         @RequestParam(required = false) Integer pagoId,
         @RequestParam(required = false) Integer sociaId,
         @RequestParam(required = false) BigDecimal montoMin,
         @RequestParam(required = false) BigDecimal montoMax,
         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(defaultValue = "fechaDetalle,desc") String sort) {

     PaginatedResponse<PagoDetalleResponse> response = pagoDetalleService.busquedaAvanzada(
             concepto, pagoId, sociaId, montoMin, montoMax, fechaInicio, fechaFin, page, size, sort);

     return ResponseEntity.ok(response);
 }

 // Búsquedas específicas útiles
 @GetMapping("/concepto")
 public ResponseEntity<PaginatedResponse<PagoDetalleResponse>> buscarPorConcepto(
         @RequestParam String concepto,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(defaultValue = "fechaDetalle,desc") String sort) {

     PaginatedResponse<PagoDetalleResponse> response = pagoDetalleService.buscarPorConcepto(
             concepto, page, size, sort);

     return ResponseEntity.ok(response);
 }

 @GetMapping("/socia/{sociaId}")
 public ResponseEntity<PaginatedResponse<PagoDetalleResponse>> buscarPorSocia(
         @PathVariable Integer sociaId,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(defaultValue = "fechaDetalle,desc") String sort) {

     PaginatedResponse<PagoDetalleResponse> response = pagoDetalleService.buscarPorSocia(
             sociaId, page, size, sort);

     return ResponseEntity.ok(response);
 }

 @GetMapping("/rango-fechas")
 public ResponseEntity<PaginatedResponse<PagoDetalleResponse>> buscarPorRangoFechas(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(defaultValue = "fechaDetalle,desc") String sort) {

     PaginatedResponse<PagoDetalleResponse> response = pagoDetalleService.buscarPorRangoFechas(
             fechaInicio, fechaFin, page, size, sort);

     return ResponseEntity.ok(response);
 }
}