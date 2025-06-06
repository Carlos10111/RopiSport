package com.ropisport.gestion.service;

import java.util.List;
import com.ropisport.gestion.model.dto.response.DashboardMetricsResponse;
import com.ropisport.gestion.model.dto.response.IngresoMensualResponse;
import com.ropisport.gestion.model.dto.response.AlertasDashboardResponse;

public interface DashboardService {
    DashboardMetricsResponse getMetricasPrincipales();
    List<IngresoMensualResponse> getIngresosPorMeses(int meses);
    AlertasDashboardResponse getAlertas();
}