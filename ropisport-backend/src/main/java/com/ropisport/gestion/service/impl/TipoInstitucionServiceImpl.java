package com.ropisport.gestion.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ropisport.gestion.exception.EntityNotFoundException;
import com.ropisport.gestion.exception.ResourceAlreadyExistsException;
import com.ropisport.gestion.model.dto.request.TipoInstitucionRequest;
import com.ropisport.gestion.model.dto.response.TipoInstitucionResponse;
import com.ropisport.gestion.model.entity.TipoInstitucion;
import com.ropisport.gestion.repository.TipoInstitucionRepository;
import com.ropisport.gestion.service.TipoInstitucionService;

@Service
public class TipoInstitucionServiceImpl implements TipoInstitucionService {

    @Autowired
    private TipoInstitucionRepository tipoInstitucionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TipoInstitucionResponse> getAllTiposInstitucion() {
        return tipoInstitucionRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TipoInstitucionResponse getTipoInstitucionById(Integer id) {
        TipoInstitucion tipo = tipoInstitucionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de institución no encontrado con ID: " + id));
        return mapToResponse(tipo);
    }

    @Override
    @Transactional
    public TipoInstitucionResponse createTipoInstitucion(TipoInstitucionRequest tipoRequest) {
        if (tipoInstitucionRepository.existsByNombre(tipoRequest.getNombre())) {
            throw new ResourceAlreadyExistsException("Ya existe un tipo de institución con ese nombre");
        }

        TipoInstitucion tipo = new TipoInstitucion();
        tipo.setNombre(tipoRequest.getNombre());
        tipo.setDescripcion(tipoRequest.getDescripcion());

        TipoInstitucion savedTipo = tipoInstitucionRepository.save(tipo);
        return mapToResponse(savedTipo);
    }

    @Override
    @Transactional
    public TipoInstitucionResponse updateTipoInstitucion(Integer id, TipoInstitucionRequest tipoRequest) {
        TipoInstitucion tipo = tipoInstitucionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de institución no encontrado con ID: " + id));

        // Verificar si el nombre ya existe en otro tipo
        if (!tipo.getNombre().equals(tipoRequest.getNombre()) &&
                tipoInstitucionRepository.existsByNombre(tipoRequest.getNombre())) {
            throw new ResourceAlreadyExistsException("Ya existe otro tipo de institución con ese nombre");
        }

        tipo.setNombre(tipoRequest.getNombre());
        tipo.setDescripcion(tipoRequest.getDescripcion());

        TipoInstitucion updatedTipo = tipoInstitucionRepository.save(tipo);
        return mapToResponse(updatedTipo);
    }

    @Override
    @Transactional
    public void deleteTipoInstitucion(Integer id) {
        TipoInstitucion tipo = tipoInstitucionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de institución no encontrado con ID: " + id));

        // Verificar si tiene instituciones asociadas
        if (tipo.getInstituciones() != null && !tipo.getInstituciones().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar el tipo de institución porque tiene instituciones asociadas");
        }

        tipoInstitucionRepository.delete(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoInstitucionResponse> searchTiposByNombre(String nombre) {
        return tipoInstitucionRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TipoInstitucionResponse mapToResponse(TipoInstitucion tipo) {
        return TipoInstitucionResponse.builder()
                .id(tipo.getId())
                .nombre(tipo.getNombre())
                .descripcion(tipo.getDescripcion())
                .createdAt(tipo.getCreatedAt())
                .updatedAt(tipo.getUpdatedAt())
                .build();
    }
}