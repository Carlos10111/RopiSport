package com.ropisport.gestion.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ropisport.gestion.model.dto.request.InstitucionRequest;
import com.ropisport.gestion.model.dto.response.InstitucionResponse;

public interface InstitucionService {
    Page<InstitucionResponse> getAllInstituciones(Pageable pageable);
    InstitucionResponse getInstitucionById(Integer id);
    // Corrige este método - hay un error de ortografía ('Intitucion' en lugar de 'Institucion')
    List<InstitucionResponse> getInstitucionesByTipoId(Integer tipoId); // Debe ser este nombre
    InstitucionResponse createInstitucion(InstitucionRequest institucionRequest);
    InstitucionResponse updateInstitucion(Integer id, InstitucionRequest institucionRequest);
    void deleteInstitucion(Integer id);
    List<InstitucionResponse> searchInstituciones(String searchTerm);
}