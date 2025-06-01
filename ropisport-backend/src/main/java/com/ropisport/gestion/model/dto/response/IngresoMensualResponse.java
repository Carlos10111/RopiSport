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
public class IngresoMensualResponse {
    private String mes;
    private Integer numeroMes;
    private Integer año;
    private BigDecimal importe;
    private Long numeroPagos;
}