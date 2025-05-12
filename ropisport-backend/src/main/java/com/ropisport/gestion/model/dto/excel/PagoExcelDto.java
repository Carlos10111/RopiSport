package com.ropisport.gestion.model.dto.excel;

import com.ropisport.gestion.model.enums.MetodoPago;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagoExcelDto {
    private Integer id;
    private String nombreSocia;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private String concepto;
    private MetodoPago metodoPago;
    private Boolean confirmado;
}