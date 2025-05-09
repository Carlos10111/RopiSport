package com.ropisport.gestion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ropisport.gestion.model.entity.CategoriaNegocio;

@Repository
public interface CategoriaNegocioRepository extends JpaRepository<CategoriaNegocio, Integer> {
    Optional<CategoriaNegocio> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}