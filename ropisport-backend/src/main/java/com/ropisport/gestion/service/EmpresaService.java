package com.ropisport.gestion.service;

import com.ropisport.gestion.model.dto.request.EmpresaRequest;
import com.ropisport.gestion.model.dto.response.EmpresaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmpresaService {
    Page<EmpresaResponse> getAllEmpresas(Pageable pageable);
    EmpresaResponse getEmpresaById(Integer id);
    EmpresaResponse getEmpresaBySociaId(Integer sociaId);
    EmpresaResponse createEmpresa(EmpresaRequest empresaRequest);
    EmpresaResponse updateEmpresa(Integer id, EmpresaRequest empresaRequest);
    void deleteEmpresa(Integer id);
    List<EmpresaResponse> searchEmpresas(String searchTerm);
    List<EmpresaResponse> getEmpresasByCategoria(Integer categoriaId);
}