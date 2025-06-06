package com.ropisport.gestion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ropisport.gestion.model.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // Métodos existentes
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRolId(Integer rolId);
    List<Usuario> findByActivo(Boolean activo);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    // NUEVOS MÉTODOS DE BÚSQUEDA
    @Query("SELECT u FROM Usuario u JOIN u.rol r WHERE " +
           "(:texto IS NULL OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(u.nombreCompleto) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(r.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))) AND " +
           "(:activo IS NULL OR u.activo = :activo) AND " +
           "(:rolId IS NULL OR r.id = :rolId)")
    Page<Usuario> busquedaGeneral(@Param("texto") String texto, 
                                  @Param("activo") Boolean activo,
                                  @Param("rolId") Integer rolId,
                                  Pageable pageable);
    
    @Query("SELECT u FROM Usuario u JOIN u.rol r WHERE " +
           "(:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))) AND " +
           "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:nombreCompleto IS NULL OR LOWER(u.nombreCompleto) LIKE LOWER(CONCAT('%', :nombreCompleto, '%'))) AND " +
           "(:rolId IS NULL OR r.id = :rolId) AND " +
           "(:activo IS NULL OR u.activo = :activo)")
    Page<Usuario> busquedaAvanzada(@Param("username") String username,
                                   @Param("email") String email,
                                   @Param("nombreCompleto") String nombreCompleto,
                                   @Param("rolId") Integer rolId,
                                   @Param("activo") Boolean activo,
                                   Pageable pageable);
}