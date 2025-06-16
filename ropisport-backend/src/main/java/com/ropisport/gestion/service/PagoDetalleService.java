package com.ropisport.gestion.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ropisport.gestion.model.dto.request.PagoDetalleRequest;
import com.ropisport.gestion.model.dto.response.PagoDetalleResponse;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;

public interface PagoDetalleService {
    
    // Métodos existentes
    List<PagoDetalleResponse> getDetallesByPagoId(Integer pagoId);
    PagoDetalleResponse getDetalleById(Integer id);
    PagoDetalleResponse createDetalle(PagoDetalleRequest detalleRequest);
    PagoDetalleResponse updateDetalle(Integer id, PagoDetalleRequest detalleRequest);
    void deleteDetalle(Integer id);
    
    // NUEVOS MÉTODOS DE BÚSQUEDA
    PaginatedResponse<PagoDetalleResponse> busquedaGeneral(
            String texto, Integer pagoId, int page, int size, String sort);
    
    PaginatedResponse<PagoDetalleResponse> busquedaAvanzada(
            String concepto,
            Integer pagoId,
            Integer sociaId,
            BigDecimal montoMin,
            BigDecimal montoMax,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin,
            int page,
            int size,
            String sort);
    
    // Búsquedas específicas útiles
    PaginatedResponse<PagoDetalleResponse> buscarPorConcepto(String concepto, int page, int size, String sort);
    PaginatedResponse<PagoDetalleResponse> buscarPorSocia(Integer sociaId, int page, int size, String sort);
    PaginatedResponse<PagoDetalleResponse> buscarPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin, int page, int size, String sort);
}