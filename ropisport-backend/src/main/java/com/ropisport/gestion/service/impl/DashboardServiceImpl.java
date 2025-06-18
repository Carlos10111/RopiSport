package com.ropisport.gestion.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ropisport.gestion.model.dto.response.AlertasDashboardResponse;
import com.ropisport.gestion.model.dto.response.DashboardMetricsResponse;
import com.ropisport.gestion.model.dto.response.IngresoMensualResponse;
import com.ropisport.gestion.model.entity.Empresa;
import com.ropisport.gestion.model.entity.Socia;
import com.ropisport.gestion.repository.EmpresaRepository;
import com.ropisport.gestion.repository.PagoRepository;
import com.ropisport.gestion.repository.SociaRepository;
import com.ropisport.gestion.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private SociaRepository sociaRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardMetricsResponse getMetricasPrincipales() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioMes = ahora.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finMes = ahora.withHour(23).withMinute(59).withSecond(59);
        LocalDateTime inicioMesAnterior = inicioMes.minusMonths(1);
        LocalDateTime finMesAnterior = inicioMes.minusDays(1).withHour(23).withMinute(59).withSecond(59);

        try {
            // ✅ MÉTRICAS EXISTENTES - mantener
            Long totalSocias = sociaRepository.count();
            Long sociasActivas = sociaRepository.countActiveSocias();
            Long nuevasSociasMes = sociaRepository.countNuevasSociasMes(inicioMes);

            Long totalEmpresas = empresaRepository.count();
            Long nuevasEmpresasMes = empresaRepository.countNuevasEmpresasMes(inicioMes);

            // Métricas de ingresos
            BigDecimal ingresosMes = pagoRepository.sumIngresosByFechaBetween(inicioMes, finMes);
            BigDecimal ingresosAnterior = pagoRepository.sumIngresosByFechaBetween(inicioMesAnterior, finMesAnterior);
            
            ingresosMes = ingresosMes != null ? ingresosMes : BigDecimal.ZERO;
            ingresosAnterior = ingresosAnterior != null ? ingresosAnterior : BigDecimal.ZERO;

            Double porcentajeCrecimiento = calcularPorcentajeCrecimiento(ingresosMes, ingresosAnterior);

            // Métricas de pagos
            Long totalPagos = pagoRepository.count();
            Long pagosPendientes = pagoRepository.countPagosPendientes(ahora.plusDays(7));
            Long pagosNoConfirmados = pagoRepository.countPagosNoConfirmados();

            // ✅ NUEVAS MÉTRICAS MANY-TO-MANY
            Long sociasConEmpresas = sociaRepository.countSociasConEmpresas();
            Long sociasEmprendedoras = sociaRepository.countSociasEmprendedoras();
            Long empresasColaborativas = empresaRepository.countEmpresasColaborativas();
            Double promedioEmpresasPorSocia = sociaRepository.promedioEmpresasPorSocia();
            Double promedioSociasPorEmpresa = empresaRepository.promedioSociasPorEmpresa();

            // ✅ TOP RANKINGS
            Pageable top5 = PageRequest.of(0, 5);
            
            Page<Socia> topSocias = sociaRepository.findSociasWithMostEmpresas(top5);
            List<DashboardMetricsResponse.TopSociaDto> topSociasDto = topSocias.getContent().stream()
                    .map(s -> new DashboardMetricsResponse.TopSociaDto(
                        s.getId(), 
                        s.getNombre(), 
                        s.getApellidos(),
                        s.getNombre() + " " + s.getApellidos(),
                        s.getEmpresas().size()))
                    .collect(Collectors.toList());

            Page<Empresa> topEmpresas = empresaRepository.findEmpresasWithMostSocias(top5);
            List<DashboardMetricsResponse.TopEmpresaDto> topEmpresasDto = topEmpresas.getContent().stream()
                    .map(e -> new DashboardMetricsResponse.TopEmpresaDto(
                        e.getId(), 
                        e.getNombreNegocio(),
                        e.getCategoria() != null ? e.getCategoria().getNombre() : "Sin categoría",
                        e.getSocias().size()))
                    .collect(Collectors.toList());

            return DashboardMetricsResponse.builder()
                    // Métricas existentes
                    .totalSocias(totalSocias != null ? totalSocias : 0L)
                    .sociasActivas(sociasActivas != null ? sociasActivas : 0L)
                    .nuevasSociasMes(nuevasSociasMes != null ? nuevasSociasMes : 0L)
                    .totalEmpresas(totalEmpresas != null ? totalEmpresas : 0L)
                    .nuevasEmpresasMes(nuevasEmpresasMes != null ? nuevasEmpresasMes : 0L)
                    .ingresosMes(ingresosMes)
                    .ingresosAnterior(ingresosAnterior)
                    .porcentajeCrecimiento(porcentajeCrecimiento)
                    .totalPagos(totalPagos != null ? totalPagos : 0L)
                    .pagosPendientes(pagosPendientes != null ? pagosPendientes : 0L)
                    .pagosNoConfirmados(pagosNoConfirmados != null ? pagosNoConfirmados : 0L)
                    // ✅ Nuevas métricas
                    .sociasConEmpresas(sociasConEmpresas != null ? sociasConEmpresas : 0L)
                    .sociasEmprendedoras(sociasEmprendedoras != null ? sociasEmprendedoras : 0L)
                    .empresasColaborativas(empresasColaborativas != null ? empresasColaborativas : 0L)
                    .promedioEmpresasPorSocia(promedioEmpresasPorSocia != null ? promedioEmpresasPorSocia : 0.0)
                    .promedioSociasPorEmpresa(promedioSociasPorEmpresa != null ? promedioSociasPorEmpresa : 0.0)
                    .topSociasEmprendedoras(topSociasDto)
                    .topEmpresasColaborativas(topEmpresasDto)
                    .build();
                    
        } catch (Exception e) {
            System.err.println("Error obteniendo métricas del dashboard: " + e.getMessage());
            e.printStackTrace();
            
            return DashboardMetricsResponse.builder()
                    .totalSocias(0L).sociasActivas(0L).nuevasSociasMes(0L)
                    .totalEmpresas(0L).nuevasEmpresasMes(0L)
                    .ingresosMes(BigDecimal.ZERO).ingresosAnterior(BigDecimal.ZERO)
                    .porcentajeCrecimiento(0.0)
                    .totalPagos(0L).pagosPendientes(0L).pagosNoConfirmados(0L)
                    // Valores por defecto para nuevas métricas
                    .sociasConEmpresas(0L).sociasEmprendedoras(0L).empresasColaborativas(0L)
                    .promedioEmpresasPorSocia(0.0).promedioSociasPorEmpresa(0.0)
                    .topSociasEmprendedoras(List.of()).topEmpresasColaborativas(List.of())
                    .build();
        }
    }

    // ✅ MANTENER MÉTODOS EXISTENTES
    @Override
    @Transactional(readOnly = true)
    public List<IngresoMensualResponse> getIngresosPorMeses(int meses) {
        try {
            LocalDateTime fechaInicio = LocalDateTime.now().minusMonths(meses - 1).withDayOfMonth(1);
            
            List<Object[]> results = pagoRepository.getIngresosPorMesesRaw(fechaInicio);
            
            return results.stream()
                    .map(this::mapearIngresoMensual)
                    .collect(Collectors.toList());
                    
        } catch (Exception e) {
            System.err.println("Error obteniendo ingresos mensuales: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AlertasDashboardResponse getAlertas() {
        try {
            LocalDateTime ahora = LocalDateTime.now();
            LocalDateTime hace30Dias = ahora.minusDays(30);
            
            Long pagosPendientes = pagoRepository.countPagosPendientes(ahora.plusDays(7));
            Long sociasMorosas = sociaRepository.countSociasMorosas(hace30Dias);
            Long pagosNoConfirmados = pagoRepository.countPagosNoConfirmados();
            Long pagosVencidos = pagoRepository.countPagosVencidos(ahora);
            
            return AlertasDashboardResponse.builder()
                    .pagosPendientes(pagosPendientes != null ? pagosPendientes : 0L)
                    .sociasMorosas(sociasMorosas != null ? sociasMorosas : 0L)
                    .renovacionesVencen(pagosVencidos != null ? pagosVencidos : 0L)
                    .pagosNoConfirmados(pagosNoConfirmados != null ? pagosNoConfirmados : 0L)
                    .empresasSinActualizar(0L)
                    .build();
                    
        } catch (Exception e) {
            System.err.println("Error obteniendo alertas: " + e.getMessage());
            e.printStackTrace();
            
            return AlertasDashboardResponse.builder()
                    .pagosPendientes(0L).sociasMorosas(0L).renovacionesVencen(0L)
                    .pagosNoConfirmados(0L).empresasSinActualizar(0L)
                    .build();
        }
    }
    
    // MÉTODOS AUXILIARES - mantener los existentes
    private Double calcularPorcentajeCrecimiento(BigDecimal actual, BigDecimal anterior) {
        if (anterior == null || anterior.compareTo(BigDecimal.ZERO) == 0) {
            return actual != null && actual.compareTo(BigDecimal.ZERO) > 0 ? 100.0 : 0.0;
        }
        if (actual == null) actual = BigDecimal.ZERO;
        
        try {
            return actual.subtract(anterior)
                    .divide(anterior, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
        } catch (ArithmeticException e) {
            return 0.0;
        }
    }
    
    private IngresoMensualResponse mapearIngresoMensual(Object[] row) {
        try {
            Integer numeroMes = row[0] != null ? ((Number) row[0]).intValue() : 1;
            Integer año = row[1] != null ? ((Number) row[1]).intValue() : LocalDateTime.now().getYear();
            BigDecimal importe = row[2] != null ? (BigDecimal) row[2] : BigDecimal.ZERO;
            Long numeroPagos = row[3] != null ? ((Number) row[3]).longValue() : 0L;
            
            String nombreMes = obtenerNombreMes(numeroMes);
            
            return IngresoMensualResponse.builder()
                    .mes(nombreMes)
                    .numeroMes(numeroMes)
                    .año(año)
                    .importe(importe)
                    .numeroPagos(numeroPagos)
                    .build();
        } catch (Exception e) {
            System.err.println("Error mapeando ingreso mensual: " + e.getMessage());
            return IngresoMensualResponse.builder()
                    .mes("Desconocido").numeroMes(1).año(2024)
                    .importe(BigDecimal.ZERO).numeroPagos(0L)
                    .build();
        }
    }

    private String obtenerNombreMes(Integer numeroMes) {
        String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                          "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return numeroMes != null && numeroMes > 0 && numeroMes <= 12 ? meses[numeroMes] : "Desconocido";
    }
}