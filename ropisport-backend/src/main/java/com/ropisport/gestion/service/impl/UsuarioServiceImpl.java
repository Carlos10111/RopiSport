package com.ropisport.gestion.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ropisport.gestion.exception.EntityNotFoundException;
import com.ropisport.gestion.exception.ResourceAlreadyExistsException;
import com.ropisport.gestion.exception.ValidationException;
import com.ropisport.gestion.model.dto.request.PasswordChangeRequest;
import com.ropisport.gestion.model.dto.request.UsuarioRequest;
import com.ropisport.gestion.model.dto.response.ApiResponse;
import com.ropisport.gestion.model.dto.response.UsuarioResponse;
import com.ropisport.gestion.model.entity.Rol;
import com.ropisport.gestion.model.entity.Usuario;
import com.ropisport.gestion.repository.RolRepository;
import com.ropisport.gestion.repository.UsuarioRepository;
import com.ropisport.gestion.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponse> getAllUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse getUsuarioById(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        return mapToResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse getUsuarioByUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con username: " + username));
        return mapToResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> getUsuariosByRol(Integer rolId) {
        return usuarioRepository.findByRolId(rolId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UsuarioResponse createUsuario(UsuarioRequest usuarioRequest) {
        // Verificar si ya existe un usuario con el mismo username o email
        if (usuarioRepository.existsByUsername(usuarioRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("Ya existe un usuario con ese nombre de usuario");
        }

        if (usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("Ya existe un usuario con ese email");
        }

        Rol rol = rolRepository.findById(usuarioRequest.getRolId())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + usuarioRequest.getRolId()));

        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioRequest.getUsername());
        usuario.setPasswordHash(passwordEncoder.encode(usuarioRequest.getPassword()));
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setNombreCompleto(usuarioRequest.getNombreCompleto());
        usuario.setRol(rol);
        usuario.setActivo(usuarioRequest.getActivo() != null ? usuarioRequest.getActivo() : true);
        usuario.setFechaCreacion(LocalDateTime.now());

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return mapToResponse(savedUsuario);
    }

    @Override
    @Transactional
    public UsuarioResponse updateUsuario(Integer id, UsuarioRequest usuarioRequest) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        // Verificar si el username ya existe en otro usuario
        if (!usuario.getUsername().equals(usuarioRequest.getUsername()) &&
                usuarioRepository.existsByUsername(usuarioRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("Ya existe otro usuario con ese nombre de usuario");
        }

        // Verificar si el email ya existe en otro usuario
        if (!usuario.getEmail().equals(usuarioRequest.getEmail()) &&
                usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("Ya existe otro usuario con ese email");
        }

        Rol rol = rolRepository.findById(usuarioRequest.getRolId())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + usuarioRequest.getRolId()));

        usuario.setUsername(usuarioRequest.getUsername());
        // Solo actualizamos la contraseña si se proporciona una nueva
        if (usuarioRequest.getPassword() != null && !usuarioRequest.getPassword().isEmpty()) {
            usuario.setPasswordHash(passwordEncoder.encode(usuarioRequest.getPassword()));
        }
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setNombreCompleto(usuarioRequest.getNombreCompleto());
        usuario.setRol(rol);
        usuario.setActivo(usuarioRequest.getActivo() != null ? usuarioRequest.getActivo() : usuario.getActivo());

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return mapToResponse(updatedUsuario);
    }

    @Override
    @Transactional
    public void deleteUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        // Verificar si tiene una socia asociada
        if (usuario.getSocia() != null) {
            throw new IllegalStateException("No se puede eliminar el usuario porque tiene una socia asociada");
        }

        usuarioRepository.delete(usuario);
    }

    @Override
    @Transactional
    public ApiResponse <Void>changePassword(Integer usuarioId, PasswordChangeRequest passwordRequest) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + usuarioId));

        // Verificar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(passwordRequest.getCurrentPassword(), usuario.getPasswordHash())) {
            throw new ValidationException("La contraseña actual es incorrecta");
        }

        // Verificar que la nueva contraseña y la confirmación coincidan
        if (!passwordRequest.getNewPassword().equals(passwordRequest.getConfirmPassword())) {
            throw new ValidationException("La nueva contraseña y la confirmación no coinciden");
        }

        // Actualizar la contraseña
        usuario.setPasswordHash(passwordEncoder.encode(passwordRequest.getNewPassword()));
        usuarioRepository.save(usuario);

        return new ApiResponse <>(true, "Contraseña actualizada correctamente");
    }
 // En UsuarioServiceImpl.java, añadir:
    @Override
    @Transactional
    public UsuarioResponse crearAdministradorSocias(UsuarioRequest usuarioRequest) {
        // Verificar si ya existe un usuario con el mismo username o email
        if (usuarioRepository.existsByUsername(usuarioRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("Ya existe un usuario con ese nombre de usuario");
        }

        if (usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("Ya existe un usuario con ese email");
        }

        // Buscar el rol de admin-socias por nombre
        Rol rolAdminSocias = rolRepository.findByNombre("ROLE_ADMIN_SOCIAS")
            .orElseThrow(() -> new EntityNotFoundException("Rol de administrador de socias no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioRequest.getUsername());
        usuario.setPasswordHash(passwordEncoder.encode(usuarioRequest.getPassword()));
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setNombreCompleto(usuarioRequest.getNombreCompleto());
        usuario.setRol(rolAdminSocias);
        usuario.setActivo(true);
        usuario.setFechaCreacion(LocalDateTime.now());

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return mapToResponse(savedUsuario);
    }
    @Override
    @Transactional
    public UsuarioResponse crearAdministradorGeneral(UsuarioRequest usuarioRequest) {
        // Verificar si ya existe un usuario con el mismo username o email
        if (usuarioRepository.existsByUsername(usuarioRequest.getUsername())) {
            throw new ResourceAlreadyExistsException("Ya existe un usuario con ese nombre de usuario");
        }

        if (usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new ResourceAlreadyExistsException("Ya existe un usuario con ese email");
        }

        // Buscar el rol de administrador general por nombre
        Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
            .orElseThrow(() -> new EntityNotFoundException("Rol de administrador general no encontrado"));

        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioRequest.getUsername());
        usuario.setPasswordHash(passwordEncoder.encode(usuarioRequest.getPassword()));
        usuario.setEmail(usuarioRequest.getEmail());
        usuario.setNombreCompleto(usuarioRequest.getNombreCompleto());
        usuario.setRol(rolAdmin);
        usuario.setActivo(true);
        usuario.setFechaCreacion(LocalDateTime.now());

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return mapToResponse(savedUsuario);
    }
    private UsuarioResponse mapToResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .nombreCompleto(usuario.getNombreCompleto())
                .rolId(usuario.getRol().getId())
                .nombreRol(usuario.getRol().getNombre())
                .activo(usuario.getActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .ultimoAcceso(usuario.getUltimoAcceso())
                .createdAt(usuario.getCreatedAt())
                .updatedAt(usuario.getUpdatedAt())
                .build();
    }


}