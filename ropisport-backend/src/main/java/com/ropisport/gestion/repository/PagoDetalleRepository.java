package com.ropisport.gestion.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ropisport.gestion.model.entity.PagoDetalle;

@Repository
public interface PagoDetalleRepository extends JpaRepository<PagoDetalle, Integer> {
    
    // Método existente
    List<PagoDetalle> findByPagoId(Integer pagoId);
    
    // NUEVOS MÉTODOS DE BÚSQUEDA
    @Query("SELECT pd FROM PagoDetalle pd JOIN pd.pago p JOIN p.socia s WHERE " +
           "(:texto IS NULL OR " +
           "LOWER(pd.concepto) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(pd.notas) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(s.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(s.apellidos) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "CAST(pd.monto AS string) LIKE CONCAT('%', :texto, '%')) AND " +
           "(:pagoId IS NULL OR p.id = :pagoId)")
    Page<PagoDetalle> busquedaGeneral(@Param("texto") String texto, 
                                      @Param("pagoId") Integer pagoId,
                                      Pageable pageable);
    
    @Query("SELECT pd FROM PagoDetalle pd JOIN pd.pago p JOIN p.socia s WHERE " +
           "(:concepto IS NULL OR LOWER(pd.concepto) LIKE LOWER(CONCAT('%', :concepto, '%'))) AND " +
           "(:pagoId IS NULL OR p.id = :pagoId) AND " +
           "(:sociaId IS NULL OR s.id = :sociaId) AND " +
           "(:montoMin IS NULL OR pd.monto >= :montoMin) AND " +
           "(:montoMax IS NULL OR pd.monto <= :montoMax) AND " +
           "(:fechaInicio IS NULL OR pd.fechaDetalle >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR pd.fechaDetalle <= :fechaFin)")
    Page<PagoDetalle> busquedaAvanzada(@Param("concepto") String concepto,
                                       @Param("pagoId") Integer pagoId,
                                       @Param("sociaId") Integer sociaId,
                                       @Param("montoMin") BigDecimal montoMin,
                                       @Param("montoMax") BigDecimal montoMax,
                                       @Param("fechaInicio") LocalDateTime fechaInicio,
                                       @Param("fechaFin") LocalDateTime fechaFin,
                                       Pageable pageable);
    
    // Búsquedas específicas útiles
    @Query("SELECT pd FROM PagoDetalle pd WHERE LOWER(pd.concepto) LIKE LOWER(CONCAT('%', :concepto, '%'))")
    Page<PagoDetalle> findByConceptoContaining(@Param("concepto") String concepto, Pageable pageable);
    
    @Query("SELECT pd FROM PagoDetalle pd JOIN pd.pago p JOIN p.socia s WHERE s.id = :sociaId")
    Page<PagoDetalle> findBySociaId(@Param("sociaId") Integer sociaId, Pageable pageable);
    
    @Query("SELECT pd FROM PagoDetalle pd WHERE pd.fechaDetalle BETWEEN :fechaInicio AND :fechaFin")
    Page<PagoDetalle> findByFechaDetalleBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                                @Param("fechaFin") LocalDateTime fechaFin, 
                                                Pageable pageable);
}