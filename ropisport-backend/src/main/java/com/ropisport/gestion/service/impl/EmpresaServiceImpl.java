package com.ropisport.gestion.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ropisport.gestion.exception.EntityNotFoundException;
import com.ropisport.gestion.model.dto.request.EmpresaRequest;
import com.ropisport.gestion.model.dto.response.EmpresaResponse;
import com.ropisport.gestion.model.dto.response.PaginatedResponse;
import com.ropisport.gestion.model.entity.CategoriaNegocio;
import com.ropisport.gestion.model.entity.Empresa;
import com.ropisport.gestion.model.entity.Socia;
import com.ropisport.gestion.repository.CategoriaNegocioRepository;
import com.ropisport.gestion.repository.EmpresaRepository;
import com.ropisport.gestion.repository.SociaRepository;
import com.ropisport.gestion.service.EmpresaService;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private SociaRepository sociaRepository;

    @Autowired
    private CategoriaNegocioRepository categoriaNegocioRepository;

    // ✅ ESTE MÉTODO DEBE EXISTIR
    @Override
    @Transactional(readOnly = true)
    public Page<EmpresaResponse> getAllEmpresas(Pageable pageable) {
        return empresaRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    // ✅ ESTE MÉTODO DEBE EXISTIR
    @Override
    @Transactional(readOnly = true)
    public EmpresaResponse getEmpresaById(Integer id) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada con ID: " + id));
        return mapToResponse(empresa);
    }

    // ✅ ESTE MÉTODO ES EL NUEVO QUE DEBES AÑADIR
    @Override
    @Transactional(readOnly = true)
    public List<EmpresaResponse> getEmpresasBySociaId(Integer sociaId) {
        // Buscar la socia primero
        Socia socia = sociaRepository.findById(sociaId)
                .orElseThrow(() -> new EntityNotFoundException("Socia no encontrada con ID: " + sociaId));
        
        // Obtener las empresas desde la socia
        return socia.getEmpresas().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ ESTOS MÉTODOS DEBEN EXISTIR
    @Override
    @Transactional
    public EmpresaResponse createEmpresa(EmpresaRequest empresaRequest) {
        // Validar categoría si se proporciona
        CategoriaNegocio categoria = null;
        if (empresaRequest.getCategoriaId() != null) {
            categoria = categoriaNegocioRepository.findById(empresaRequest.getCategoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
        }

        // Validar y obtener socias si se proporcionan
        List<Socia> socias = new ArrayList<>();
        if (empresaRequest.getSociaIds() != null && !empresaRequest.getSociaIds().isEmpty()) {
            socias = sociaRepository.findAllById(empresaRequest.getSociaIds());
            if (socias.size() != empresaRequest.getSociaIds().size()) {
                throw new EntityNotFoundException("Una o más socias no fueron encontradas");
            }
        }

        // Crear empresa
        Empresa empresa = Empresa.builder()
                .nombreNegocio(empresaRequest.getNombreNegocio())
                .descripcionNegocio(empresaRequest.getDescripcionNegocio())
                .categoria(categoria)
                .direccion(empresaRequest.getDireccion())
                .telefonoNegocio(empresaRequest.getTelefonoNegocio())
                .emailNegocio(empresaRequest.getEmailNegocio())
                .cif(empresaRequest.getCif())
                .epigrafe(empresaRequest.getEpigrafe())
                .web(empresaRequest.getWeb())
                .instagram(empresaRequest.getInstagram())
                .facebook(empresaRequest.getFacebook())
                .linkedin(empresaRequest.getLinkedin())
                .otrasRedes(empresaRequest.getOtrasRedes())
                .socias(socias)
                .build();

        Empresa empresaGuardada = empresaRepository.save(empresa);

        // Actualizar la relación en las socias
        for (Socia socia : socias) {
            if (!socia.getEmpresas().contains(empresaGuardada)) {
                socia.getEmpresas().add(empresaGuardada);
            }
        }

        return mapToResponse(empresaGuardada);
    }

    @Override
    @Transactional
    public EmpresaResponse updateEmpresa(Integer id, EmpresaRequest empresaRequest) {
        Empresa empresa = empresaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada con ID: " + id));

        // Validar categoría si se proporciona
        CategoriaNegocio categoria = null;
        if (empresaRequest.getCategoriaId() != null) {
            categoria = categoriaNegocioRepository.findById(empresaRequest.getCategoriaId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
        }

        // Obtener socias actuales para limpiar la relación
        List<Socia> sociasActuales = new ArrayList<>(empresa.getSocias());
        
        // Limpiar relaciones actuales
        for (Socia socia : sociasActuales) {
            socia.getEmpresas().remove(empresa);
        }
        empresa.getSocias().clear();

        // Validar y obtener nuevas socias
        List<Socia> nuevasSocias = new ArrayList<>();
        if (empresaRequest.getSociaIds() != null && !empresaRequest.getSociaIds().isEmpty()) {
            nuevasSocias = sociaRepository.findAllById(empresaRequest.getSociaIds());
            if (nuevasSocias.size() != empresaRequest.getSociaIds().size()) {
                throw new EntityNotFoundException("Una o más socias no fueron encontradas");
            }
        }

        // Actualizar empresa
        empresa.setNombreNegocio(empresaRequest.getNombreNegocio());
        empresa.setDescripcionNegocio(empresaRequest.getDescripcionNegocio());
        empresa.setCategoria(categoria);
        empresa.setDireccion(empresaRequest.getDireccion());
        empresa.setTelefonoNegocio(empresaRequest.getTelefonoNegocio());
        empresa.setEmailNegocio(empresaRequest.getEmailNegocio());
        empresa.setCif(empresaRequest.getCif());
        empresa.setEpigrafe(empresaRequest.getEpigrafe());
        empresa.setWeb(empresaRequest.getWeb());
        empresa.setInstagram(empresaRequest.getInstagram());
        empresa.setFacebook(empresaRequest.getFacebook());
        empresa.setLinkedin(empresaRequest.getLinkedin());
        empresa.setOtrasRedes(empresaRequest.getOtrasRedes());
        empresa.setSocias(nuevasSocias);

        Empresa empresaActualizada = empresaRepository.save(empresa);

        // Establecer nuevas relaciones
        for (Socia socia : nuevasSocias) {
            if (!socia.getEmpresas().contains(empresaActualizada)) {
                socia.getEmpresas().add(empresaActualizada);
            }
        }

        return mapToResponse(empresaActualizada);
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

    @Override
    public PaginatedResponse<EmpresaResponse> busquedaGeneral(
            String texto, Boolean activa, int page, int size, String sort) {
        
        Pageable pageable = createPageable(page, size, sort);
        Page<Empresa> empresas = empresaRepository.busquedaGeneral(texto, activa, pageable);
        
        List<EmpresaResponse> content = empresas.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return createPaginatedResponse(content, empresas);
    }

    @Override
    public PaginatedResponse<EmpresaResponse> busquedaAvanzada(
            String nombreNegocio,
            String cif,
            String email,
            String telefono,
            String direccion,
            Integer categoriaId,
            Boolean activa,
            int page,
            int size,
            String sort) {
        
        Pageable pageable = createPageable(page, size, sort);
        Page<Empresa> empresas = empresaRepository.busquedaAvanzada(
                nombreNegocio, cif, email, telefono, direccion, categoriaId, activa, pageable);
        
        List<EmpresaResponse> content = empresas.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
        return createPaginatedResponse(content, empresas);
    }

    // MÉTODOS PRIVADOS DE MAPEO
    private EmpresaResponse mapToResponse(Empresa empresa) {
        List<EmpresaResponse.SociaDto> sociasDto = empresa.getSocias().stream()
                .map(socia -> new EmpresaResponse.SociaDto(
                    socia.getId(),
                    socia.getNombre(),
                    socia.getApellidos(),
                    socia.getNombre() + " " + socia.getApellidos()
                ))
                .collect(Collectors.toList());

        return EmpresaResponse.builder()
                .id(empresa.getId())
                .socias(sociasDto)
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

    // Métodos auxiliares
    private Pageable createPageable(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        return PageRequest.of(page, size, Sort.by(direction, sortField));
    }

    private PaginatedResponse<EmpresaResponse> createPaginatedResponse(
            List<EmpresaResponse> content, Page<Empresa> page) {
        return new PaginatedResponse<>(
            content,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isLast()
        );
    }
}