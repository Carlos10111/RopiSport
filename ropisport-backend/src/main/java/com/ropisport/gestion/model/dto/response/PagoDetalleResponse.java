package com.ropisport.gestion.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoDetalleResponse {
    private Integer id;
    private Integer pagoId;
    private String concepto;
    private BigDecimal monto;
    private LocalDateTime fechaDetalle;
    private String notas;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}