package com.ropisport.gestion.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ropisport.gestion.model.dto.response.AlertasDashboardResponse;
import com.ropisport.gestion.model.dto.response.DashboardMetricsResponse;
import com.ropisport.gestion.model.dto.response.IngresoMensualResponse;
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
        LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finMes = LocalDateTime.now();
        LocalDateTime inicioMesAnterior = inicioMes.minusMonths(1);
        LocalDateTime finMesAnterior = inicioMes.minusDays(1);

        // Métricas de socias
        Long totalSocias = sociaRepository.count();
        Long sociasActivas = sociaRepository.countActiveSocias();
        Long nuevasSociasMes = sociaRepository.countNuevasSociasMes(inicioMes);

        // Métricas de empresas
        Long totalEmpresas = empresaRepository.count();
        Long nuevasEmpresasMes = empresaRepository.countNuevasEmpresasMes(inicioMes);

        // Métricas de ingresos
        BigDecimal ingresosMes = pagoRepository.sumIngresosByFechaBetween(inicioMes, finMes);
        BigDecimal ingresosAnterior = pagoRepository.sumIngresosByFechaBetween(inicioMesAnterior, finMesAnterior);
        
        if (ingresosMes == null) ingresosMes = BigDecimal.ZERO;
        if (ingresosAnterior == null) ingresosAnterior = BigDecimal.ZERO;

        // Calcular porcentaje de crecimiento
        Double porcentajeCrecimiento = 0.0;
        if (ingresosAnterior.compareTo(BigDecimal.ZERO) > 0) {
            porcentajeCrecimiento = ingresosMes.subtract(ingresosAnterior)
                    .divide(ingresosAnterior, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .doubleValue();
        }

        // Métricas de pagos
        Long totalPagos = pagoRepository.count();
        Long pagosPendientes = pagoRepository.countPagosPendientes(LocalDateTime.now().minusDays(30));
        Long pagosNoConfirmados = pagoRepository.countPagosNoConfirmados();

        return DashboardMetricsResponse.builder()
                .totalSocias(totalSocias)
                .sociasActivas(sociasActivas)
                .nuevasSociasMes(nuevasSociasMes)
                .totalEmpresas(totalEmpresas)
                .nuevasEmpresasMes(nuevasEmpresasMes)
                .ingresosMes(ingresosMes)
                .ingresosAnterior(ingresosAnterior)
                .porcentajeCrecimiento(porcentajeCrecimiento)
                .totalPagos(totalPagos)
                .pagosPendientes(pagosPendientes)
                .pagosNoConfirmados(pagosNoConfirmados)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngresoMensualResponse> getIngresosPorMeses(int meses) {
        LocalDateTime fechaInicio = LocalDateTime.now().minusMonths(meses).withDayOfMonth(1);
        
        // Usar la query raw y mapear manualmente
        List<Object[]> results = pagoRepository.getIngresosPorMesesRaw(fechaInicio);        
        return results.stream()
                .map(row -> {
                    Integer numeroMes = (Integer) row[0];
                    Integer año = (Integer) row[1];
                    BigDecimal importe = (BigDecimal) row[2];
                    Long numeroPagos = (Long) row[3];
                    
                    // Convertir número de mes a nombre
                    String nombreMes = obtenerNombreMes(numeroMes);
                    
                    return IngresoMensualResponse.builder()
                            .mes(nombreMes)
                            .numeroMes(numeroMes)
                            .año(año)
                            .importe(importe)
                            .numeroPagos(numeroPagos)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // Método auxiliar
    private String obtenerNombreMes(Integer numeroMes) {
        String[] meses = {"", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                          "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        return numeroMes != null && numeroMes > 0 && numeroMes <= 12 ? meses[numeroMes] : "Desconocido";
    }
    
        
    @Override
    @Transactional(readOnly = true)
    public AlertasDashboardResponse getAlertas() {
        LocalDateTime hace30Dias = LocalDateTime.now().minusDays(30);
        
        Long pagosPendientes = pagoRepository.countPagosPendientes(hace30Dias);
        Long sociasMorosas = sociaRepository.countSociasMorosas(hace30Dias);
        Long pagosNoConfirmados = pagoRepository.countPagosNoConfirmados();
        
        return AlertasDashboardResponse.builder()
                .pagosPendientes(pagosPendientes)
                .sociasMorosas(sociasMorosas)
                .pagosNoConfirmados(pagosNoConfirmados)
                .empresasSinActualizar(0L) // Por implementar si es necesario
                .build();
    }
}