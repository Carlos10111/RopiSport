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

import com.ropisport.gestion.model.entity.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
    
    // Métodos existentes
    List<Pago> findBySociaId(Integer sociaId);
    List<Pago> findByFechaPagoBetween(LocalDateTime inicio, LocalDateTime fin);
    List<Pago> findByConfirmado(Boolean confirmado);
    List<Pago> findByConceptoContainingIgnoreCase(String concepto);
    
    // BÚSQUEDA GENERAL
    @Query("SELECT p FROM Pago p JOIN p.socia s WHERE " +
           "(:texto IS NULL OR " +
           "LOWER(p.concepto) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(s.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(s.apellidos) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "CAST(p.monto AS string) LIKE CONCAT('%', :texto, '%')) AND " +
           "(:confirmado IS NULL OR p.confirmado = :confirmado)")
    Page<Pago> busquedaGeneral(@Param("texto") String texto, 
                               @Param("confirmado") Boolean confirmado, 
                               Pageable pageable);
    
    // BÚSQUEDA AVANZADA
    @Query("SELECT p FROM Pago p JOIN p.socia s WHERE " +
           "(:concepto IS NULL OR LOWER(p.concepto) LIKE LOWER(CONCAT('%', :concepto, '%'))) AND " +
           "(:sociaId IS NULL OR s.id = :sociaId) AND " +
           "(:importeMin IS NULL OR p.monto >= :importeMin) AND " +
           "(:importeMax IS NULL OR p.monto <= :importeMax) AND " +
           "(:confirmado IS NULL OR p.confirmado = :confirmado) AND " +
           "(:fechaInicio IS NULL OR p.fechaPago >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR p.fechaPago <= :fechaFin)")
    Page<Pago> busquedaAvanzada(@Param("concepto") String concepto,
                                @Param("sociaId") Integer sociaId,
                                @Param("importeMin") BigDecimal importeMin,
                                @Param("importeMax") BigDecimal importeMax,
                                @Param("confirmado") Boolean confirmado,
                                @Param("fechaInicio") LocalDateTime fechaInicio,
                                @Param("fechaFin") LocalDateTime fechaFin,
                                Pageable pageable);
    
    // MÉTODOS PARA DASHBOARD - MEJORADOS
    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Pago p WHERE " +
           "p.fechaPago >= :fechaInicio AND p.fechaPago <= :fechaFin AND p.confirmado = true")
    BigDecimal sumIngresosByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                        @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT COUNT(p) FROM Pago p WHERE p.confirmado = false OR p.confirmado IS NULL")
    Long countPagosNoConfirmados();
    
    // MEJORAR: Pagos pendientes son los que no están confirmados y están cerca del vencimiento
    @Query("SELECT COUNT(p) FROM Pago p WHERE p.confirmado = false AND " +
           "(p.fechaVencimiento IS NULL OR p.fechaVencimiento < :fechaLimite)")
    Long countPagosPendientes(@Param("fechaLimite") LocalDateTime fechaLimite);
    
    // AGREGAR: Método para contar pagos vencidos
    @Query("SELECT COUNT(p) FROM Pago p WHERE p.confirmado = false AND " +
           "p.fechaVencimiento < :fechaActual")
    Long countPagosVencidos(@Param("fechaActual") LocalDateTime fechaActual);
    
    // MEJORAR: Query de ingresos mensuales con mejor formato
    @Query("SELECT " +
           "FUNCTION('MONTH', p.fechaPago) as numeroMes, " +
           "FUNCTION('YEAR', p.fechaPago) as año, " +
           "COALESCE(SUM(p.monto), 0) as importe, " +
           "COUNT(p) as numeroPagos " +
           "FROM Pago p " +
           "WHERE p.fechaPago >= :fechaInicio AND p.confirmado = true " +
           "GROUP BY FUNCTION('YEAR', p.fechaPago), FUNCTION('MONTH', p.fechaPago) " +
           "ORDER BY año DESC, numeroMes DESC")
    List<Object[]> getIngresosPorMesesRaw(@Param("fechaInicio") LocalDateTime fechaInicio);
    
    // AGREGAR: Estadísticas adicionales útiles
    @Query("SELECT COUNT(p) FROM Pago p WHERE p.confirmado = true")
    Long countPagosConfirmados();
    
    @Query("SELECT AVG(p.monto) FROM Pago p WHERE p.confirmado = true")
    BigDecimal getPromedioMontoPagos();
    
    @Query("SELECT SUM(p.monto) FROM Pago p WHERE " +
           "p.confirmado = true AND FUNCTION('YEAR', p.fechaPago) = :año")
    BigDecimal sumIngresosByAño(@Param("año") Integer año);
    
    // AGREGAR: Top socias por pagos
    @Query("SELECT s.nombre, s.apellidos, SUM(p.monto) as total " +
           "FROM Pago p JOIN p.socia s " +
           "WHERE p.confirmado = true AND p.fechaPago >= :fechaInicio " +
           "GROUP BY s.id, s.nombre, s.apellidos " +
           "ORDER BY total DESC")
    List<Object[]> getTopSociasByIngresos(@Param("fechaInicio") LocalDateTime fechaInicio);
}