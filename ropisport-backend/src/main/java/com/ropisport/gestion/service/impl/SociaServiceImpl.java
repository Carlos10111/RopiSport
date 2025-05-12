package com.ropisport.gestion.service.impl;

import com.ropisport.gestion.exception.EntityNotFoundException;
import com.ropisport.gestion.exception.ResourceAlreadyExistsException;
import com.ropisport.gestion.model.dto.request.SociaRequest;
import com.ropisport.gestion.model.dto.response.SociaResponse;
import com.ropisport.gestion.model.entity.CategoriaNegocio;
import com.ropisport.gestion.model.entity.Socia;
import com.ropisport.gestion.model.entity.Usuario;
import com.ropisport.gestion.repository.CategoriaNegocioRepository;
import com.ropisport.gestion.repository.SociaRepository;
import com.ropisport.gestion.repository.UsuarioRepository;
import com.ropisport.gestion.service.SociaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SociaServiceImpl implements SociaService {

    @Autowired
    private SociaRepository sociaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private CategoriaNegocioRepository categoriaNegocioRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<SociaResponse> getAllSocias(Pageable pageable) {
        return sociaRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SociaResponse getSociaById(Integer id) {
        Socia socia = sociaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Socia no encontrada con ID: " + id));
        return mapToResponse(socia);
    }

    @Override
    @Transactional(readOnly = true)
    public SociaResponse getSociaByNumero(String numeroSocia) {
        Socia socia = sociaRepository.findByNumeroSocia(numeroSocia)
                .orElseThrow(() -> new EntityNotFoundException("Socia no encontrada con número: " + numeroSocia));
        return mapToResponse(socia);
    }

    @Override
    @Transactional(readOnly = true)
    public SociaResponse getSociaByUsuarioId(Integer usuarioId) {
        Socia socia = sociaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Socia no encontrada para usuario con ID: " + usuarioId));
        return mapToResponse(socia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SociaResponse> getSociasByActiva(Boolean activa) {
        return sociaRepository.findByActiva(activa).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SociaResponse> getSociasByCategoria(Integer categoriaId) {
        return sociaRepository.findByCategoriaId(categoriaId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SociaResponse createSocia(SociaRequest sociaRequest) {
        // Verificar si ya existe una socia con el mismo número
        if (sociaRequest.getNumeroSocia() != null && 
                sociaRepository.findByNumeroSocia(sociaRequest.getNumeroSocia()).isPresent()) {
            throw new ResourceAlreadyExistsException("Ya existe una socia con el número: " + sociaRequest.getNumeroSocia());
        }
        
        Usuario usuario = null;
        if (sociaRequest.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(sociaRequest.getUsuarioId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + sociaRequest.getUsuarioId()));
            
            // Verificar si el usuario ya está asociado a otra socia
            if (sociaRepository.findByUsuarioId(usuario.getId()).isPresent()) {
                throw new ResourceAlreadyExistsException("El usuario ya está asociado a otra socia");
            }
        }
        
        CategoriaNegocio categoria = null;
        if (sociaRequest.getCategoriaId() != null) {
            categoria = categoriaNegocioRepository.findById(sociaRequest.getCategoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + sociaRequest.getCategoriaId()));
        }
        
        Socia socia = new Socia();
        mapRequestToEntity(sociaRequest, socia, usuario, categoria);
        
        Socia savedSocia = sociaRepository.save(socia);
        return mapToResponse(savedSocia);
    }

    @Override
    @Transactional
    public SociaResponse updateSocia(Integer id, SociaRequest sociaRequest) {
        Socia socia = sociaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Socia no encontrada con ID: " + id));
        
        // Verificar si ya existe otra socia con el mismo número
        if (sociaRequest.getNumeroSocia() != null && 
                !sociaRequest.getNumeroSocia().equals(socia.getNumeroSocia()) && 
                sociaRepository.findByNumeroSocia(sociaRequest.getNumeroSocia()).isPresent()) {
            throw new ResourceAlreadyExistsException("Ya existe otra socia con el número: " + sociaRequest.getNumeroSocia());
        }
        
        Usuario usuario = null;
        if (sociaRequest.getUsuarioId() != null) {
            usuario = usuarioRepository.findById(sociaRequest.getUsuarioId())
                    .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + sociaRequest.getUsuarioId()));
            
            // Verificar si el usuario ya está asociado a otra socia
            Optional<Socia> sociaConUsuario = sociaRepository.findByUsuarioId(usuario.getId());
            if (sociaConUsuario.isPresent() && !sociaConUsuario.get().getId().equals(id)) {
                throw new ResourceAlreadyExistsException("El usuario ya está asociado a otra socia");
            }
        }
        
        CategoriaNegocio categoria = null;
        if (sociaRequest.getCategoriaId() != null) {
            categoria = categoriaNegocioRepository.findById(sociaRequest.getCategoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + sociaRequest.getCategoriaId()));
        }
        
        mapRequestToEntity(sociaRequest, socia, usuario, categoria);
        
        Socia updatedSocia = sociaRepository.save(socia);
        return mapToResponse(updatedSocia);
    }

    @Override
    @Transactional
    public void deleteSocia(Integer id) {
        Socia socia = sociaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Socia no encontrada con ID: " + id));
        
        // Verificar si tiene pagos o empresas asociadas
        if ((socia.getPagos() != null && !socia.getPagos().isEmpty()) || 
            (socia.getEmpresas() != null && !socia.getEmpresas().isEmpty())) {
            throw new IllegalStateException("No se puede eliminar la socia porque tiene pagos o empresas asociadas");
        }
        
        sociaRepository.delete(socia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SociaResponse> searchSocias(String searchTerm) {
        return sociaRepository.searchByTerm(searchTerm).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private void mapRequestToEntity(SociaRequest request, Socia socia, Usuario usuario, CategoriaNegocio categoria) {
        socia.setNumeroSocia(request.getNumeroSocia());
        socia.setNombre(request.getNombre());
        socia.setApellidos(request.getApellidos());
        socia.setUsuario(usuario);
        socia.setNombreNegocio(request.getNombreNegocio());
        socia.setDescripcionNegocio(request.getDescripcionNegocio());
        socia.setCategoria(categoria);
        socia.setDireccion(request.getDireccion());
        socia.setTelefonoPersonal(request.getTelefonoPersonal());
        socia.setTelefonoNegocio(request.getTelefonoNegocio());
        socia.setEmail(request.getEmail());
        socia.setCif(request.getCif());
        socia.setNumeroCuenta(request.getNumeroCuenta());
        socia.setEpigrafe(request.getEpigrafe());
        socia.setActiva(request.getActiva());
        socia.setFechaInicio(request.getFechaInicio());
        socia.setFechaBaja(request.getFechaBaja());
        socia.setObservaciones(request.getObservaciones());
    }
    
    private SociaResponse mapToResponse(Socia socia) {
        return SociaResponse.builder()
                .id(socia.getId())
                .numeroSocia(socia.getNumeroSocia())
                .nombre(socia.getNombre())
                .apellidos(socia.getApellidos())
                .usuarioId(socia.getUsuario() != null ? socia.getUsuario().getId() : null)
                .username(socia.getUsuario() != null ? socia.getUsuario().getUsername() : null)
                .nombreNegocio(socia.getNombreNegocio())
                .descripcionNegocio(socia.getDescripcionNegocio())
                .categoriaId(socia.getCategoria() != null ? socia.getCategoria().getId() : null)
                .nombreCategoria(socia.getCategoria() != null ? socia.getCategoria().getNombre() : null)
                .direccion(socia.getDireccion())
                .telefonoPersonal(socia.getTelefonoPersonal())
                .telefonoNegocio(socia.getTelefonoNegocio())
                .email(socia.getEmail())
                .cif(socia.getCif())
                .numeroCuenta(socia.getNumeroCuenta())
                .epigrafe(socia.getEpigrafe())
                .activa(socia.getActiva())
                .fechaInicio(socia.getFechaInicio())
                .fechaBaja(socia.getFechaBaja())
                .observaciones(socia.getObservaciones())
                .createdAt(socia.getCreatedAt())
                .updatedAt(socia.getUpdatedAt())
                .build();
    }
}