package com.ropisport.gestion.service;

import com.ropisport.gestion.model.dto.request.SociaRequest;
import com.ropisport.gestion.model.dto.response.SociaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SociaService {
    Page<SociaResponse> getAllSocias(Pageable pageable);
    SociaResponse getSociaById(Integer id);
    SociaResponse getSociaByNumero(String numeroSocia);
    SociaResponse getSociaByUsuarioId(Integer usuarioId);
    List<SociaResponse> getSociasByActiva(Boolean activa);
    List<SociaResponse> getSociasByCategoria(Integer categoriaId);
    SociaResponse createSocia(SociaRequest sociaRequest);
    SociaResponse updateSocia(Integer id, SociaRequest sociaRequest);
    void deleteSocia(Integer id);
    List<SociaResponse> searchSocias(String searchTerm);
}