package com.ropisport.gestion.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ropisport.gestion.exception.EntityNotFoundException;
import com.ropisport.gestion.exception.ResourceAlreadyExistsException;
import com.ropisport.gestion.model.dto.request.RolRequest;
import com.ropisport.gestion.model.dto.response.RolResponse;
import com.ropisport.gestion.model.entity.Rol;
import com.ropisport.gestion.repository.RolRepository;
import com.ropisport.gestion.service.RolService;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RolResponse> getAllRoles() {
        return rolRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RolResponse getRolById(Integer id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));
        return mapToResponse(rol);
    }

    @Override
    @Transactional
    public RolResponse createRol(RolRequest rolRequest) {
        if (rolRepository.existsByNombre(rolRequest.getNombre())) {
            throw new ResourceAlreadyExistsException("Ya existe un rol con ese nombre");
        }

        Rol rol = new Rol();
        rol.setNombre(rolRequest.getNombre());
        rol.setDescripcion(rolRequest.getDescripcion());

        Rol savedRol = rolRepository.save(rol);
        return mapToResponse(savedRol);
    }

    @Override
    @Transactional
    public RolResponse updateRol(Integer id, RolRequest rolRequest) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));

        // Verificar si el nombre ya existe en otro rol
        if (!rol.getNombre().equals(rolRequest.getNombre()) &&
                rolRepository.existsByNombre(rolRequest.getNombre())) {
            throw new ResourceAlreadyExistsException("Ya existe otro rol con ese nombre");
        }

        rol.setNombre(rolRequest.getNombre());
        rol.setDescripcion(rolRequest.getDescripcion());

        Rol updatedRol = rolRepository.save(rol);
        return mapToResponse(updatedRol);
    }

    @Override
    @Transactional
    public void deleteRol(Integer id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));

        // Verificar si tiene usuarios asociados
        if (rol.getUsuarios() != null && !rol.getUsuarios().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar el rol porque tiene usuarios asociados");
        }

        rolRepository.delete(rol);
    }

    private RolResponse mapToResponse(Rol rol) {
        return RolResponse.builder()
                .id(rol.getId())
                .nombre(rol.getNombre())
                .descripcion(rol.getDescripcion())
                .createdAt(rol.getCreatedAt())
                .updatedAt(rol.getUpdatedAt())
                .build();
    }
}