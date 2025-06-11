package com.ropisport.gestion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ropisport.gestion.model.dto.response.AlertasDashboardResponse;
import com.ropisport.gestion.model.dto.response.DashboardMetricsResponse;
import com.ropisport.gestion.model.dto.response.IngresoMensualResponse;
import com.ropisport.gestion.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/metricas")
    public ResponseEntity<DashboardMetricsResponse> getMetricasPrincipales() {
        DashboardMetricsResponse metricas = dashboardService.getMetricasPrincipales();
        return ResponseEntity.ok(metricas);
    }

    @GetMapping("/ingresos-mensuales")
    public ResponseEntity<List<IngresoMensualResponse>> getIngresosMensuales(
            @RequestParam(defaultValue = "6") int meses) {
        List<IngresoMensualResponse> ingresos = dashboardService.getIngresosPorMeses(meses);
        return ResponseEntity.ok(ingresos);
    }

    @GetMapping("/alertas")
    public ResponseEntity<AlertasDashboardResponse> getAlertas() {
        AlertasDashboardResponse alertas = dashboardService.getAlertas();
        return ResponseEntity.ok(alertas);
    }
}