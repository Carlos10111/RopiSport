package com.ropisport.gestion.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ropisport.gestion.model.dto.request.PasswordChangeRequest;
import com.ropisport.gestion.model.dto.request.UsuarioRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
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
    /**
     * Crea un nuevo usuario con rol de administrador de socias
     * @param usuarioRequest Datos del nuevo usuario
     * @return Datos del usuario creado
     */
    UsuarioResponse crearAdministradorSocias(UsuarioRequest usuarioRequest);

    /**
     * Crea un nuevo usuario con rol de administrador general
     * @param usuarioRequest Datos del nuevo usuario
     * @return Datos del usuario creado
     */
    UsuarioResponse crearAdministradorGeneral(UsuarioRequest usuarioRequest);
}