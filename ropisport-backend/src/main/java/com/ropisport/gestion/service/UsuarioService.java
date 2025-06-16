package com.ropisport.gestion.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ropisport.gestion.model.dto.request.PasswordChangeRequest;
import com.ropisport.gestion.model.dto.request.UsuarioRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;
import com.ropisport.gestion.model.dto.response.UsuarioResponse;

public interface UsuarioService {
    
    Page<UsuarioResponse> getAllUsuarios(Pageable pageable);
    UsuarioResponse getUsuarioById(Integer id);
    UsuarioResponse getUsuarioByUsername(String username);
    List<UsuarioResponse> getUsuariosByRol(Integer rolId);
    UsuarioResponse createUsuario(UsuarioRequest usuarioRequest);
    UsuarioResponse updateUsuario(Integer id, UsuarioRequest usuarioRequest);
    void deleteUsuario(Integer id);
    ApiResponse<Void> changePassword(Integer usuarioId, PasswordChangeRequest passwordRequest);
    UsuarioResponse crearAdministradorSocias(UsuarioRequest usuarioRequest);
    UsuarioResponse crearAdministradorGeneral(UsuarioRequest usuarioRequest);
    
    //  MÉTODOS BÚSQUEDA
    PaginatedResponse<UsuarioResponse> busquedaGeneral(
            String texto, Boolean activo, Integer rolId, int page, int size, String sort);
    
    PaginatedResponse<UsuarioResponse> busquedaAvanzada(
            String username,
            String email,
            String nombreCompleto,
            Integer rolId,
            Boolean activo,
            int page,
            int size,
            String sort);
}