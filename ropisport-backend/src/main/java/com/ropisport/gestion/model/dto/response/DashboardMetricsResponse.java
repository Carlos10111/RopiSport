package com.ropisport.gestion.model.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardMetricsResponse {
    private Long totalSocias;
    private Long sociasActivas;
    private Long nuevasSociasMes;
    private Long totalEmpresas;
    private Long nuevasEmpresasMes;
    private BigDecimal ingresosMes;
    private BigDecimal ingresosAnterior;
    private Double porcentajeCrecimiento;
    private Long totalPagos;
    private Long pagosPendientes;
    private Long pagosNoConfirmados;
}