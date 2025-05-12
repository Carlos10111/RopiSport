package com.ropisport.gestion.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.ropisport.gestion.model.enums.MetodoPago;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoRequest {
    
    @NotNull(message = "El ID de la socia es obligatorio")
    private Integer sociaId;
    
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser un valor positivo")
    private BigDecimal monto;
    
    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDateTime fechaPago;
    
    @Size(max = 200, message = "El concepto no puede tener más de 200 caracteres")
    private String concepto;
    
    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPago metodoPago;
    
    private Boolean confirmado;
    
    private List<PagoDetalleRequest> detalles;
}