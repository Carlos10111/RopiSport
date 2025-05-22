package com.ropisport.gestion.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ropisport.gestion.model.entity.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    List<Pago> findBySociaId(Integer sociaId);
    List<Pago> findByFechaPagoBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Pago> findByConfirmado(Boolean confirmado);
    List<Pago> findByConceptoContainingIgnoreCase(String concepto);
}