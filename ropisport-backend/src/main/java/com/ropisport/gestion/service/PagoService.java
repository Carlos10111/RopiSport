package com.ropisport.gestion.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ropisport.gestion.model.dto.request.PagoRequest;
import com.ropisport.gestion.model.dto.response.PagoResponse;

public interface PagoService {
    Page<PagoResponse> getAllPagos(Pageable pageable);
    PagoResponse getPagoById(Integer id);
    List<PagoResponse> getPagosBySociaId(Integer sociaId);
    List<PagoResponse> getPagosByFechaBetween(LocalDateTime inicio, LocalDateTime fin);
    List<PagoResponse> getPagosByConfirmado(Boolean confirmado);
    PagoResponse createPago(PagoRequest pagoRequest);
    PagoResponse updatePago(Integer id, PagoRequest pagoRequest);
    void deletePago(Integer id);
}