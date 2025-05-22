package com.ropisport.gestion.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ropisport.gestion.model.enums.MetodoPago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoResponse {
    private Integer id;
    private Integer sociaId;
    private String nombreSocia;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private String concepto;
    private MetodoPago metodoPago;
    private Boolean confirmado;
    private List<PagoDetalleResponse> detalles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}