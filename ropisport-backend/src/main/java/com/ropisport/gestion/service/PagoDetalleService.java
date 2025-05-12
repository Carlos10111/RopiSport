package com.ropisport.gestion.service;

import com.ropisport.gestion.model.dto.request.PagoDetalleRequest;
import com.ropisport.gestion.model.dto.response.PagoDetalleResponse;

import java.util.List;

public interface PagoDetalleService {
    List<PagoDetalleResponse> getDetallesByPagoId(Integer pagoId);
    PagoDetalleResponse getDetalleById(Integer id);
    PagoDetalleResponse createDetalle(PagoDetalleRequest detalleRequest);
    PagoDetalleResponse updateDetalle(Integer id, PagoDetalleRequest detalleRequest);
    void deleteDetalle(Integer id);
}