package com.ropisport.gestion.repository;

import com.ropisport.gestion.model.entity.CategoriaNegocio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaNegocioRepository extends JpaRepository<CategoriaNegocio, Integer> {
    Optional<CategoriaNegocio> findByNombre(String nombre);
    List<CategoriaNegocio> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByNombre(String nombre);
}