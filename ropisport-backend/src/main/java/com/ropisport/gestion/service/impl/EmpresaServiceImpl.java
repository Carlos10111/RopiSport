package com.ropisport.gestion.service.impl;

import com.ropisport.gestion.exception.EntityNotFoundException;
import com.ropisport.gestion.model.dto.request.EmpresaRequest;
import com.ropisport.gestion.model.dto.response.EmpresaResponse;
import com.ropisport.gestion.model.entity.CategoriaNegocio;
import com.ropisport.gestion.model.entity.Empresa;
import com.ropisport.gestion.model.entity.Socia;
import com.ropisport.gestion.repository.CategoriaNegocioRepository;
import com.ropisport.gestion.repository.EmpresaRepository;
import com.ropisport.gestion.repository.SociaRepository;
import com.ropisport.gestion.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;
    
    @Autowired
    private SociaRepository sociaRepository;
    
    @Autowired
    private CategoriaNegocioRepository categoriaNegocioRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<EmpresaResponse> getAllEmpresas(Pageable pageable) {
        return empresaRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public EmpresaResponse getEmpresaById(Integer id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada con ID: " + id));
        return mapToResponse(empresa);
    }

    @Override
    @Transactional(readOnly = true)
    public EmpresaResponse getEmpresaBySociaId(Integer sociaId) {
        Empresa empresa = empresaRepository.findBySociaId(sociaId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada para la socia con ID: " + sociaId));
        return mapToResponse(empresa);
    }

    @Override
    @Transactional
    public EmpresaResponse createEmpresa(EmpresaRequest empresaRequest) {
        Socia socia = sociaRepository.findById(empresaRequest.getSociaId())
                .orElseThrow(() -> new EntityNotFoundException("Socia no encontrada con ID: " + empresaRequest.getSociaId()));
        
        // Verificar si la socia ya tiene una empresa
        Optional<Empresa> existingEmpresa = empresaRepository.findBySociaId(socia.getId());
        if (existingEmpresa.isPresent()) {
            throw new IllegalStateException("La socia ya tiene una empresa asociada");
        }
        
        CategoriaNegocio categoria = null;
        if (empresaRequest.getCategoriaId() != null) {
            categoria = categoriaNegocioRepository.findById(empresaRequest.getCategoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + empresaRequest.getCategoriaId()));
        }
        
        Empresa empresa = new Empresa();
        mapRequestToEntity(empresaRequest, empresa, socia, categoria);
        
        Empresa savedEmpresa = empresaRepository.save(empresa);
        return mapToResponse(savedEmpresa);
    }

    @Override
    @Transactional
    public EmpresaResponse updateEmpresa(Integer id, EmpresaRequest empresaRequest) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada con ID: " + id));
        
        // Solo verificamos que exista la socia, pero no cambiamos la asociación
        if (!empresa.getSocia().getId().equals(empresaRequest.getSociaId())) {
            throw new IllegalStateException("No se puede cambiar la socia asociada a una empresa");
        }
        
        CategoriaNegocio categoria = null;
        if (empresaRequest.getCategoriaId() != null) {
            categoria = categoriaNegocioRepository.findById(empresaRequest.getCategoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + empresaRequest.getCategoriaId()));
        }
        
        mapRequestToEntity(empresaRequest, empresa, empresa.getSocia(), categoria);
        
        Empresa updatedEmpresa = empresaRepository.save(empresa);
        return mapToResponse(updatedEmpresa);
    }

    @Override
    @Transactional
    public void deleteEmpresa(Integer id) {
        if (!empresaRepository.existsById(id)) {
            throw new EntityNotFoundException("Empresa no encontrada con ID: " + id);
        }
        empresaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpresaResponse> searchEmpresas(String searchTerm) {
        return empresaRepository.searchByTerm(searchTerm).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpresaResponse> getEmpresasByCategoria(Integer categoriaId) {
        return empresaRepository.findByCategoriaId(categoriaId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private void mapRequestToEntity(EmpresaRequest request, Empresa empresa, Socia socia, CategoriaNegocio categoria) {
        empresa.setSocia(socia);
        empresa.setNombreNegocio(request.getNombreNegocio());
        empresa.setDescripcionNegocio(request.getDescripcionNegocio());
        empresa.setCategoria(categoria);
        empresa.setDireccion(request.getDireccion());
        empresa.setTelefonoNegocio(request.getTelefonoNegocio());
        empresa.setEmailNegocio(request.getEmailNegocio());
        empresa.setCif(request.getCif());
        empresa.setEpigrafe(request.getEpigrafe());
        empresa.setWeb(request.getWeb());
        empresa.setInstagram(request.getInstagram());
        empresa.setFacebook(request.getFacebook());
        empresa.setLinkedin(request.getLinkedin());
        empresa.setOtrasRedes(request.getOtrasRedes());
    }
    
    private EmpresaResponse mapToResponse(Empresa empresa) {
        return EmpresaResponse.builder()
                .id(empresa.getId())
                .sociaId(empresa.getSocia().getId())
                .nombreSocia(empresa.getSocia().getNombre() + " " + empresa.getSocia().getApellidos())
                .nombreNegocio(empresa.getNombreNegocio())
                .descripcionNegocio(empresa.getDescripcionNegocio())
                .categoriaId(empresa.getCategoria() != null ? empresa.getCategoria().getId() : null)
                .nombreCategoria(empresa.getCategoria() != null ? empresa.getCategoria().getNombre() : null)
                .direccion(empresa.getDireccion())
                .telefonoNegocio(empresa.getTelefonoNegocio())
                .emailNegocio(empresa.getEmailNegocio())
                .cif(empresa.getCif())
                .epigrafe(empresa.getEpigrafe())
                .web(empresa.getWeb())
                .instagram(empresa.getInstagram())
                .facebook(empresa.getFacebook())
                .linkedin(empresa.getLinkedin())
                .otrasRedes(empresa.getOtrasRedes())
                .createdAt(empresa.getCreatedAt())
                .updatedAt(empresa.getUpdatedAt())
                .build();
    }
}