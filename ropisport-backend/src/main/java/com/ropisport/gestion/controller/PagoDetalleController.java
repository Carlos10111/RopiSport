package com.ropisport.gestion.controller;

import com.ropisport.gestion.model.dto.request.PagoDetalleRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.PagoDetalleResponse;
import com.ropisport.gestion.service.PagoDetalleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

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
    public ResponseEntity<ApiResponse> deleteDetalle(@PathVariable Integer id) {
        pagoDetalleService.deleteDetalle(id);
        return ResponseEntity.ok(new ApiResponse(true, "Detalle de pago eliminado correctamente"));
    }
}