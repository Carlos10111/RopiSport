package com.ropisport.gestion.service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ropisport.gestion.model.dto.request.EmpresaRequest;
import com.ropisport.gestion.model.dto.response.EmpresaResponse;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;

public interface EmpresaService {
    
    // Métodos existentes
    Page<EmpresaResponse> getAllEmpresas(Pageable pageable);
    EmpresaResponse getEmpresaById(Integer id);
    
    // ❌ CAMBIAR ESTO:
    // EmpresaResponse getEmpresaBySociaId(Integer sociaId);
    // ✅ POR ESTO:
    List<EmpresaResponse> getEmpresasBySociaId(Integer sociaId);
    
    EmpresaResponse createEmpresa(EmpresaRequest empresaRequest);
    EmpresaResponse updateEmpresa(Integer id, EmpresaRequest empresaRequest);
    void deleteEmpresa(Integer id);
    List<EmpresaResponse> searchEmpresas(String searchTerm);
    List<EmpresaResponse> getEmpresasByCategoria(Integer categoriaId);
    
    // NUEVOS MÉTODOS DE BÚSQUEDA
    PaginatedResponse<EmpresaResponse> busquedaGeneral(
            String texto, Boolean activa, int page, int size, String sort);
    
    PaginatedResponse<EmpresaResponse> busquedaAvanzada(
            String nombreNegocio,
            String cif,
            String email,
            String telefono,
            String direccion,
            Integer categoriaId,
            Boolean activa,
            int page,
            int size,
            String sort);
    
    // Exportación
}