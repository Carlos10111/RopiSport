package com.ropisport.gestion.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ropisport.gestion.model.entity.Socia;

@Repository
public interface SociaRepository extends JpaRepository<Socia, Integer>, JpaSpecificationExecutor<Socia> {

    // Búsqueda por nombre
    Page<Socia> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

    // Búsqueda por apellidos
    Page<Socia> findByApellidosContainingIgnoreCase(String apellidos, Pageable pageable);

    // Búsqueda por nombre de negocio
    Page<Socia> findByNombreNegocioContainingIgnoreCase(String nombreNegocio, Pageable pageable);

    // Búsqueda por email
    Page<Socia> findByEmailContainingIgnoreCase(String email, Pageable pageable);

    // Búsqueda por estado (activa/inactiva)
    Page<Socia> findByActiva(Boolean activa, Pageable pageable);

    // Búsqueda por CIF
    Page<Socia> findByCifContaining(String cif, Pageable pageable);

    // Búsqueda por teléfono (personal o negocio)
    Page<Socia> findByTelefonoPersonalContainingOrTelefonoNegocioContaining(
        String telefonoPersonal, String telefonoNegocio, Pageable pageable);

    // Búsqueda combinada
    @Query("SELECT s FROM Socia s WHERE " +
           "(:texto IS NULL OR " +
           "   LOWER(s.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "   LOWER(s.apellidos) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "   LOWER(s.nombreNegocio) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "   LOWER(s.email) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "   s.cif LIKE CONCAT('%', :texto, '%') OR " +
           "   s.telefonoPersonal LIKE CONCAT('%', :texto, '%') OR " +
           "   s.telefonoNegocio LIKE CONCAT('%', :texto, '%') OR " +
           "   LOWER(s.observaciones) LIKE LOWER(CONCAT('%', :texto, '%'))) " +
           "AND (:activa IS NULL OR s.activa = :activa)")
    Page<Socia> busquedaGeneral(
            @Param("texto") String texto,
            @Param("activa") Boolean activa,
            Pageable pageable);

    // Búsqueda avanzada con múltiples criterios
    @Query("SELECT s FROM Socia s LEFT JOIN s.categoria c WHERE " +
           "(:nombre IS NULL OR LOWER(s.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')) OR " +
           "   LOWER(s.apellidos) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:nombreNegocio IS NULL OR LOWER(s.nombreNegocio) LIKE LOWER(CONCAT('%', :nombreNegocio, '%'))) AND " +
           "(:email IS NULL OR LOWER(s.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:telefono IS NULL OR s.telefonoPersonal LIKE CONCAT('%', :telefono, '%') OR " +
           "   s.telefonoNegocio LIKE CONCAT('%', :telefono, '%')) AND " +
           "(:cif IS NULL OR s.cif LIKE CONCAT('%', :cif, '%')) AND " +
           "(:categoriaId IS NULL OR c.id = :categoriaId) AND " +
           "(:activa IS NULL OR s.activa = :activa)")
    Page<Socia> busquedaAvanzada(
            @Param("nombre") String nombre,
            @Param("nombreNegocio") String nombreNegocio,
            @Param("email") String email,
            @Param("telefono") String telefono,
            @Param("cif") String cif,
            @Param("categoriaId") Integer categoriaId,
            @Param("activa") Boolean activa,
            Pageable pageable);
}