package com.ropisport.gestion.repository;

import com.ropisport.gestion.model.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    Optional<Empresa> findBySociaId(Integer sociaId);
    List<Empresa> findByNombreNegocioContainingIgnoreCase(String nombreNegocio);
    List<Empresa> findByCategoriaId(Integer categoriaId);
    
    @Query("SELECT e FROM Empresa e WHERE " +
           "LOWER(e.nombreNegocio) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.descripcionNegocio) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(e.emailNegocio) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "e.telefonoNegocio LIKE CONCAT('%', :searchTerm, '%') OR " +
           "e.cif LIKE CONCAT('%', :searchTerm, '%')")
    List<Empresa> searchByTerm(@Param("searchTerm") String searchTerm);
}