package com.ropisport.gestion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ropisport.gestion.model.entity.Institucion;

@Repository
public interface InstitucionRepository extends JpaRepository<Institucion, Integer> {
    List<Institucion> findByNombreInstitucionContainingIgnoreCase(String nombre);
    List<Institucion> findByPersonaContactoContainingIgnoreCase(String contacto);
    List<Institucion> findByEmailContainingIgnoreCase(String email);
    List<Institucion> findByTelefonoContaining(String telefono);
    List<Institucion> findByTipoInstitucionId(Integer tipoId);

    @Query("SELECT i FROM Institucion i WHERE " +
           "LOWER(i.nombreInstitucion) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.personaContacto) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "i.telefono LIKE CONCAT('%', :searchTerm, '%')")
    List<Institucion> searchByTerm(@Param("searchTerm") String searchTerm);
}