package com.ropisport.gestion.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ropisport.gestion.model.entity.Socia;

@Repository
public interface SociaRepository extends JpaRepository<Socia, Integer> {
    Optional<Socia> findByUsuarioId(Integer usuarioId);
    
    Optional<Socia> findByNumeroSocia(String numeroSocia);
    
    List<Socia> findByCategoriaId(Integer categoriaId);
    
    @Query("SELECT s FROM Socia s WHERE " +
           "LOWER(s.nombre) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(s.apellidos) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(s.nombreNegocio) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(s.telefonoPersonal) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(s.telefonoNegocio) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<Socia> buscarPorTermino(@Param("term") String term, Pageable pageable);
    
    List<Socia> findByActiva(Boolean activa);
}
