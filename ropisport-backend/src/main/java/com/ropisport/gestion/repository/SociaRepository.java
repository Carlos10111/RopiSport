package com.ropisport.gestion.repository;

import com.ropisport.gestion.model.entity.Socia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SociaRepository extends JpaRepository<Socia, Integer> {
    Optional<Socia> findByNumeroSocia(String numeroSocia);
    List<Socia> findByNombreContainingIgnoreCase(String nombre);
    List<Socia> findByApellidosContainingIgnoreCase(String apellidos);
    List<Socia> findByNombreNegocioContainingIgnoreCase(String nombreNegocio);
    List<Socia> findByEmailContainingIgnoreCase(String email);
    List<Socia> findByTelefonoPersonalContaining(String telefono);
    List<Socia> findByTelefonoNegocioContaining(String telefono);
    List<Socia> findByCategoriaId(Integer categoriaId);
    List<Socia> findByActiva(Boolean activa);
    Optional<Socia> findByUsuarioId(Integer usuarioId);
    
    @Query("SELECT s FROM Socia s WHERE " +
           "LOWER(s.nombre) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.apellidos) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.nombreNegocio) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "s.telefonoPersonal LIKE CONCAT('%', :searchTerm, '%') OR " +
           "s.telefonoNegocio LIKE CONCAT('%', :searchTerm, '%') OR " +
           "s.numeroSocia LIKE CONCAT('%', :searchTerm, '%')")
    List<Socia> searchByTerm(@Param("searchTerm") String searchTerm);
}