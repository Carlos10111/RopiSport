package com.ropisport.gestion.service;

import java.util.List;

import com.ropisport.gestion.model.dto.request.PagoDetalleRequest;
import com.ropisport.gestion.model.dto.response.PagoDetalleResponse;

public interface PagoDetalleService {
    List<PagoDetalleResponse> getDetallesByPagoId(Integer pagoId);
    PagoDetalleResponse getDetalleById(Integer id);
    PagoDetalleResponse createDetalle(PagoDetalleRequest detalleRequest);
    PagoDetalleResponse updateDetalle(Integer id, PagoDetalleRequest detalleRequest);
    void deleteDetalle(Integer id);
}