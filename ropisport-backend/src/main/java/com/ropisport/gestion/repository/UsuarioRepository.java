package com.ropisport.gestion.repository;

import com.ropisport.gestion.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRolId(Integer rolId);
    List<Usuario> findByActivo(Boolean activo);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}