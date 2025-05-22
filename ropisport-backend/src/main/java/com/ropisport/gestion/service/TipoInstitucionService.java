package com.ropisport.gestion.service;

import java.util.List;

import com.ropisport.gestion.model.dto.request.TipoInstitucionRequest;
import com.ropisport.gestion.model.dto.response.TipoInstitucionResponse;

public interface TipoInstitucionService {
    List<TipoInstitucionResponse> getAllTiposInstitucion();
    TipoInstitucionResponse getTipoInstitucionById(Integer id);
    TipoInstitucionResponse createTipoInstitucion(TipoInstitucionRequest tipoRequest);
    TipoInstitucionResponse updateTipoInstitucion(Integer id, TipoInstitucionRequest tipoRequest);
    void deleteTipoInstitucion(Integer id);
    List<TipoInstitucionResponse> searchTiposByNombre(String nombre);
}