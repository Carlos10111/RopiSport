package com.ropisport.gestion.repository;

import com.ropisport.gestion.model.entity.PagoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoDetalleRepository extends JpaRepository<PagoDetalle, Integer> {
    List<PagoDetalle> findByPagoId(Integer pagoId);
}