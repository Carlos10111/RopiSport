package com.ropisport.gestion.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ropisport.gestion.exception.EntityNotFoundException;
import com.ropisport.gestion.model.dto.request.InstitucionRequest;
import com.ropisport.gestion.model.dto.response.InstitucionResponse;
import com.ropisport.gestion.model.entity.Institucion;
import com.ropisport.gestion.model.entity.TipoInstitucion;
import com.ropisport.gestion.repository.InstitucionRepository;
import com.ropisport.gestion.repository.TipoInstitucionRepository;
import com.ropisport.gestion.service.InstitucionService;

@Service
public class InstitucionServiceImpl implements InstitucionService {

    @Autowired
    private InstitucionRepository institucionRepository;

    @Autowired
    private TipoInstitucionRepository tipoInstitucionRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<InstitucionResponse> getAllInstituciones(Pageable pageable) {
        return institucionRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public InstitucionResponse getInstitucionById(Integer id) {
        Institucion institucion = institucionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Institución no encontrada con ID: " + id));
        return mapToResponse(institucion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstitucionResponse> getInstitucionesByTipoId(Integer tipoId) {
        return institucionRepository.findByTipoInstitucionId(tipoId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InstitucionResponse createInstitucion(InstitucionRequest institucionRequest) {
        TipoInstitucion tipoInstitucion = tipoInstitucionRepository.findById(institucionRequest.getTipoInstitucionId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de institución no encontrado con ID: " + institucionRequest.getTipoInstitucionId()));

        Institucion institucion = new Institucion();
        mapRequestToEntity(institucionRequest, institucion, tipoInstitucion);

        Institucion savedInstitucion = institucionRepository.save(institucion);
        return mapToResponse(savedInstitucion);
    }

    @Override
    @Transactional
    public InstitucionResponse updateInstitucion(Integer id, InstitucionRequest institucionRequest) {
        Institucion institucion = institucionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Institución no encontrada con ID: " + id));

        TipoInstitucion tipoInstitucion = tipoInstitucionRepository.findById(institucionRequest.getTipoInstitucionId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de institución no encontrado con ID: " + institucionRequest.getTipoInstitucionId()));

        mapRequestToEntity(institucionRequest, institucion, tipoInstitucion);

        Institucion updatedInstitucion = institucionRepository.save(institucion);
        return mapToResponse(updatedInstitucion);
    }

    @Override
    @Transactional
    public void deleteInstitucion(Integer id) {
        if (!institucionRepository.existsById(id)) {
            throw new EntityNotFoundException("Institución no encontrada con ID: " + id);
        }
        institucionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstitucionResponse> searchInstituciones(String searchTerm) {
        return institucionRepository.searchByTerm(searchTerm).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private void mapRequestToEntity(InstitucionRequest request, Institucion institucion, TipoInstitucion tipoInstitucion) {
        institucion.setNombreInstitucion(request.getNombreInstitucion());
        institucion.setPersonaContacto(request.getPersonaContacto());
        institucion.setCargo(request.getCargo());
        institucion.setTelefono(request.getTelefono());
        institucion.setEmail(request.getEmail());
        institucion.setWeb(request.getWeb());
        institucion.setTipoInstitucion(tipoInstitucion);
        institucion.setObservaciones(request.getObservaciones());
    }

    private InstitucionResponse mapToResponse(Institucion institucion) {
        return InstitucionResponse.builder()
                .id(institucion.getId())
                .nombreInstitucion(institucion.getNombreInstitucion())
                .personaContacto(institucion.getPersonaContacto())
                .cargo(institucion.getCargo())
                .telefono(institucion.getTelefono())
                .email(institucion.getEmail())
                .web(institucion.getWeb())
                .tipoInstitucionId(institucion.getTipoInstitucion().getId())
                .nombreTipoInstitucion(institucion.getTipoInstitucion().getNombre())
                .observaciones(institucion.getObservaciones())
                .createdAt(institucion.getCreatedAt())
                .updatedAt(institucion.getUpdatedAt())
                .build();
    }
}