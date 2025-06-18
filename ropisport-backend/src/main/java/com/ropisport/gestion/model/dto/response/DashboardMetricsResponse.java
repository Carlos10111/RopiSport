package com.ropisport.gestion.model.dto.response;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
 
    private Long sociasConEmpresas;           
    private Long sociasEmprendedoras;        
    private Long empresasColaborativas;       
    private Double promedioEmpresasPorSocia;  
    private Double promedioSociasPorEmpresa;  
    
    // ✅ TOP RANKINGS
    private List<TopSociaDto> topSociasEmprendedoras;
    private List<TopEmpresaDto> topEmpresasColaborativas;
    
    @Data
    @AllArgsConstructor
    public static class TopSociaDto {
        private Integer id;
        private String nombre;
        private String apellidos;
        private String nombreCompleto;
        private Integer cantidadEmpresas;
    }
    
    @Data
    @AllArgsConstructor
    public static class TopEmpresaDto {
        private Integer id;
        private String nombreNegocio;
        private String categoria;
        private Integer cantidadSocias;
    }
}