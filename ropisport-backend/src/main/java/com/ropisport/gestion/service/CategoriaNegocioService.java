package com.ropisport.gestion.service;

import java.util.List;

import com.ropisport.gestion.model.dto.request.CategoriaRequest;
import com.ropisport.gestion.model.dto.response.CategoriaResponse;

public interface CategoriaNegocioService {
    List<CategoriaResponse> getAllCategorias();
    CategoriaResponse getCategoriaById(Integer id);
    CategoriaResponse createCategoria(CategoriaRequest categoriaRequest);
    CategoriaResponse updateCategoria(Integer id, CategoriaRequest categoriaRequest);
    void deleteCategoria(Integer id);
    List<CategoriaResponse> searchCategoriasByNombre(String nombre);
}