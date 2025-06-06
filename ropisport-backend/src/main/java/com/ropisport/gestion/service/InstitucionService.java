package com.ropisport.gestion.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ropisport.gestion.model.dto.request.InstitucionRequest;
import com.ropisport.gestion.model.dto.response.InstitucionResponse;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;

public interface InstitucionService {
    
    Page<InstitucionResponse> getAllInstituciones(Pageable pageable);
    InstitucionResponse getInstitucionById(Integer id);
    List<InstitucionResponse> getInstitucionesByTipoId(Integer tipoId);
    InstitucionResponse createInstitucion(InstitucionRequest institucionRequest);
    InstitucionResponse updateInstitucion(Integer id, InstitucionRequest institucionRequest);
    void deleteInstitucion(Integer id);
    List<InstitucionResponse> searchInstituciones(String searchTerm);
    
    // MÉTODOS BÚSQUEDA
    PaginatedResponse<InstitucionResponse> busquedaGeneral(
            String texto, Integer tipoInstitucionId, int page, int size, String sort);
    
    PaginatedResponse<InstitucionResponse> busquedaAvanzada(
            String nombreInstitucion,
            String personaContacto,
            String email,
            String telefono,
            String cargo,
            Integer tipoInstitucionId,
            int page,
            int size,
            String sort);
}