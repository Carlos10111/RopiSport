package com.ropisport.gestion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ropisport.gestion.model.entity.TipoInstitucion;

@Repository
public interface TipoInstitucionRepository extends JpaRepository<TipoInstitucion, Integer> {
    Optional<TipoInstitucion> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}