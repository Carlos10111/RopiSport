package com.ropisport.gestion.service;

import com.ropisport.gestion.model.dto.request.TipoInstitucionRequest;
import com.ropisport.gestion.model.dto.response.TipoInstitucionResponse;

import java.util.List;

public interface TipoInstitucionService {
    List<TipoInstitucionResponse> getAllTiposInstitucion();
    TipoInstitucionResponse getTipoInstitucionById(Integer id);
    TipoInstitucionResponse createTipoInstitucion(TipoInstitucionRequest tipoRequest);
    TipoInstitucionResponse updateTipoInstitucion(Integer id, TipoInstitucionRequest tipoRequest);
    void deleteTipoInstitucion(Integer id);
    List<TipoInstitucionResponse> searchTiposByNombre(String nombre);
}