package com.ropisport.gestion.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertasDashboardResponse {
    private Long pagosPendientes;
    private Long sociasMorosas;
    private Long renovacionesVencen;
    private Long pagosNoConfirmados;
    private Long empresasSinActualizar;
}