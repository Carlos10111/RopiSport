package com.ropisport.gestion.service;

import java.util.Map;

import com.ropisport.gestion.model.dto.request.SociaRequest;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;
import com.ropisport.gestion.model.dto.response.SociaResponse;

public interface SociaService {

    // Métodos CRUD
    SociaResponse getSociaById(Integer id);
    PaginatedResponse<SociaResponse> getAllSocias(int page, int size, String sort);
    SociaResponse createSocia(SociaRequest request);
    SociaResponse updateSocia(Integer id, SociaRequest request);
    void deleteSocia(Integer id);

    // Métodos de búsqueda
    PaginatedResponse<SociaResponse> busquedaGeneral(
            String texto, Boolean activa, int page, int size, String sort);

    PaginatedResponse<SociaResponse> busquedaAvanzada(
            String nombre,
            String nombreNegocio,
            String email,
            String telefono,
            String cif,
            Integer categoriaId,
            Boolean activa,
            int page,
            int size,
            String sort);

    // Cambio de estado
    SociaResponse cambiarEstado(Integer id, Boolean activa, String observaciones);

    // Exportación
    byte[] exportToExcel(Map<String, String> parametros);
}