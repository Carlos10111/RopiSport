package com.ropisport.gestion.service;

import com.ropisport.gestion.model.dto.request.PasswordChangeRequest;
import com.ropisport.gestion.model.dto.request.UsuarioRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.UsuarioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsuarioService {
    Page<UsuarioResponse> getAllUsuarios(Pageable pageable);
    UsuarioResponse getUsuarioById(Integer id);
    UsuarioResponse getUsuarioByUsername(String username);
    List<UsuarioResponse> getUsuariosByRol(Integer rolId);
    UsuarioResponse createUsuario(UsuarioRequest usuarioRequest);
    UsuarioResponse updateUsuario(Integer id, UsuarioRequest usuarioRequest);
    void deleteUsuario(Integer id);
    ApiResponse changePassword(Integer usuarioId, PasswordChangeRequest passwordRequest);
}