package com.ropisport.gestion.model.dto.excel;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PagoDetalleExcelDto {
    private Integer id;
    private Integer pagoId;
    private String concepto;
    private BigDecimal monto;
    private LocalDateTime fechaDetalle;
    private String notas;
}