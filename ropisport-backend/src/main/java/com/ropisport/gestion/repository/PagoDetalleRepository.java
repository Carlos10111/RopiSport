package com.ropisport.gestion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ropisport.gestion.model.entity.PagoDetalle;

@Repository
public interface PagoDetalleRepository extends JpaRepository<PagoDetalle, Integer> {
    List<PagoDetalle> findByPagoId(Integer pagoId);
}