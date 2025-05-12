package com.ropisport.gestion.service.impl;

import com.ropisport.gestion.exception.EntityNotFoundException;
import com.ropisport.gestion.exception.ResourceAlreadyExistsException;
import com.ropisport.gestion.model.dto.request.CategoriaRequest;
import com.ropisport.gestion.model.dto.response.CategoriaResponse;
import com.ropisport.gestion.model.entity.CategoriaNegocio;
import com.ropisport.gestion.repository.CategoriaNegocioRepository;
import com.ropisport.gestion.service.CategoriaNegocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaNegocioServiceImpl implements CategoriaNegocioService {

    @Autowired
    private CategoriaNegocioRepository categoriaNegocioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> getAllCategorias() {
        return categoriaNegocioRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse getCategoriaById(Integer id) {
        CategoriaNegocio categoria = categoriaNegocioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
        return mapToResponse(categoria);
    }

    @Override
    @Transactional
    public CategoriaResponse createCategoria(CategoriaRequest categoriaRequest) {
        if (categoriaNegocioRepository.existsByNombre(categoriaRequest.getNombre())) {
            throw new ResourceAlreadyExistsException("Ya existe una categoría con ese nombre");
        }
        
        CategoriaNegocio categoria = new CategoriaNegocio();
        categoria.setNombre(categoriaRequest.getNombre());
        categoria.setDescripcion(categoriaRequest.getDescripcion());
        
        CategoriaNegocio savedCategoria = categoriaNegocioRepository.save(categoria);
        return mapToResponse(savedCategoria);
    }

    @Override
    @Transactional
    public CategoriaResponse updateCategoria(Integer id, CategoriaRequest categoriaRequest) {
        CategoriaNegocio categoria = categoriaNegocioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
        
        // Verificar si el nombre ya existe en otra categoría
        if (!categoria.getNombre().equals(categoriaRequest.getNombre()) && 
                categoriaNegocioRepository.existsByNombre(categoriaRequest.getNombre())) {
            throw new ResourceAlreadyExistsException("Ya existe otra categoría con ese nombre");
        }
        
        categoria.setNombre(categoriaRequest.getNombre());
        categoria.setDescripcion(categoriaRequest.getDescripcion());
        
        CategoriaNegocio updatedCategoria = categoriaNegocioRepository.save(categoria);
        return mapToResponse(updatedCategoria);
    }

    @Override
    @Transactional
    public void deleteCategoria(Integer id) {
        CategoriaNegocio categoria = categoriaNegocioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + id));
        
        // Verificar si tiene empresas o socias asociadas
        if ((categoria.getEmpresas() != null && !categoria.getEmpresas().isEmpty()) || 
            (categoria.getSocias() != null && !categoria.getSocias().isEmpty())) {
            throw new IllegalStateException("No se puede eliminar la categoría porque tiene empresas o socias asociadas");
        }
        
        categoriaNegocioRepository.delete(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> searchCategoriasByNombre(String nombre) {
        return categoriaNegocioRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private CategoriaResponse mapToResponse(CategoriaNegocio categoria) {
        return CategoriaResponse.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .createdAt(categoria.getCreatedAt())
                .updatedAt(categoria.getUpdatedAt())
                .createdBy(categoria.getCreatedBy())
                .updatedBy(categoria.getUpdatedBy())
                .build();
    }
}