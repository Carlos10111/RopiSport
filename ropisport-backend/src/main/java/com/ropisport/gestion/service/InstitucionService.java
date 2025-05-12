package com.ropisport.gestion.service;

import com.ropisport.gestion.model.dto.request.InstitucionRequest;
import com.ropisport.gestion.model.dto.response.InstitucionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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