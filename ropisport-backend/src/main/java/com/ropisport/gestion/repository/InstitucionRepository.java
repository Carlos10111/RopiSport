package com.ropisport.gestion.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ropisport.gestion.model.entity.Institucion;

@Repository
public interface InstitucionRepository extends JpaRepository<Institucion, Integer> {
    
    // Métodos existentes
    List<Institucion> findByTipoInstitucionId(Integer tipoInstitucionId);
    
    @Query("SELECT i FROM Institucion i WHERE " +
           "LOWER(i.nombreInstitucion) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.personaContacto) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(i.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "i.telefono LIKE CONCAT('%', :searchTerm, '%')")
    List<Institucion> searchByTerm(@Param("searchTerm") String searchTerm);
    
    // NUEVOS MÉTODOS DE BÚSQUEDA PAGINADA
    @Query("SELECT i FROM Institucion i JOIN i.tipoInstitucion t WHERE " +
           "(:texto IS NULL OR " +
           "LOWER(i.nombreInstitucion) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(i.personaContacto) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(i.email) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(i.cargo) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(t.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "i.telefono LIKE CONCAT('%', :texto, '%')) AND " +
           "(:tipoInstitucionId IS NULL OR t.id = :tipoInstitucionId)")
    Page<Institucion> busquedaGeneral(@Param("texto") String texto, 
                                      @Param("tipoInstitucionId") Integer tipoInstitucionId, 
                                      Pageable pageable);
    
    @Query("SELECT i FROM Institucion i JOIN i.tipoInstitucion t WHERE " +
           "(:nombreInstitucion IS NULL OR LOWER(i.nombreInstitucion) LIKE LOWER(CONCAT('%', :nombreInstitucion, '%'))) AND " +
           "(:personaContacto IS NULL OR LOWER(i.personaContacto) LIKE LOWER(CONCAT('%', :personaContacto, '%'))) AND " +
           "(:email IS NULL OR LOWER(i.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:telefono IS NULL OR i.telefono LIKE CONCAT('%', :telefono, '%')) AND " +
           "(:cargo IS NULL OR LOWER(i.cargo) LIKE LOWER(CONCAT('%', :cargo, '%'))) AND " +
           "(:tipoInstitucionId IS NULL OR t.id = :tipoInstitucionId)")
    Page<Institucion> busquedaAvanzada(@Param("nombreInstitucion") String nombreInstitucion,
                                       @Param("personaContacto") String personaContacto,
                                       @Param("email") String email,
                                       @Param("telefono") String telefono,
                                       @Param("cargo") String cargo,
                                       @Param("tipoInstitucionId") Integer tipoInstitucionId,
                                       Pageable pageable);
}