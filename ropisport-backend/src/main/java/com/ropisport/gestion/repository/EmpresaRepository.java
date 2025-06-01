package com.ropisport.gestion.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ropisport.gestion.model.entity.Empresa;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    
    // Métodos existentes
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
    
    // NUEVOS MÉTODOS DE BÚSQUEDA PAGINADA
    @Query("SELECT e FROM Empresa e JOIN e.socia s WHERE " +
           "(:texto IS NULL OR " +
           "LOWER(e.nombreNegocio) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(e.descripcionNegocio) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(e.emailNegocio) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(s.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(s.apellidos) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "e.telefonoNegocio LIKE CONCAT('%', :texto, '%') OR " +
           "e.cif LIKE CONCAT('%', :texto, '%')) AND " +
           "(:activa IS NULL OR s.activa = :activa)")
    Page<Empresa> busquedaGeneral(@Param("texto") String texto, 
                                  @Param("activa") Boolean activa, 
                                  Pageable pageable);
    
    @Query("SELECT e FROM Empresa e JOIN e.socia s WHERE " +
           "(:nombreNegocio IS NULL OR LOWER(e.nombreNegocio) LIKE LOWER(CONCAT('%', :nombreNegocio, '%'))) AND " +
           "(:cif IS NULL OR e.cif LIKE CONCAT('%', :cif, '%')) AND " +
           "(:email IS NULL OR LOWER(e.emailNegocio) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:telefono IS NULL OR e.telefonoNegocio LIKE CONCAT('%', :telefono, '%')) AND " +
           "(:direccion IS NULL OR LOWER(e.direccion) LIKE LOWER(CONCAT('%', :direccion, '%'))) AND " +
           "(:categoriaId IS NULL OR e.categoria.id = :categoriaId) AND " +
           "(:activa IS NULL OR s.activa = :activa)")
    Page<Empresa> busquedaAvanzada(@Param("nombreNegocio") String nombreNegocio,
                                   @Param("cif") String cif,
                                   @Param("email") String email,
                                   @Param("telefono") String telefono,
                                   @Param("direccion") String direccion,
                                   @Param("categoriaId") Integer categoriaId,
                                   @Param("activa") Boolean activa,
                                   Pageable pageable);
    @Query("SELECT COUNT(e) FROM Empresa e JOIN e.socia s WHERE s.activa = true")
    Long countActiveEmpresas();

    @Query("SELECT COUNT(e) FROM Empresa e WHERE e.createdAt >= :fechaInicio")
    Long countNuevasEmpresasMes(@Param("fechaInicio") LocalDateTime fechaInicio);
}