package com.ropisport.gestion.service;

import com.ropisport.gestion.model.dto.request.CategoriaRequest;
import com.ropisport.gestion.model.dto.response.CategoriaResponse;

import java.util.List;

public interface CategoriaNegocioService {
    List<CategoriaResponse> getAllCategorias();
    CategoriaResponse getCategoriaById(Integer id);
    CategoriaResponse createCategoria(CategoriaRequest categoriaRequest);
    CategoriaResponse updateCategoria(Integer id, CategoriaRequest categoriaRequest);
    void deleteCategoria(Integer id);
    List<CategoriaResponse> searchCategoriasByNombre(String nombre);
}