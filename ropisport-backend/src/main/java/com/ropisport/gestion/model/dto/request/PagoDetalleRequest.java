package com.ropisport.gestion.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
public class PagoDetalleRequest {
    
    @NotNull(message = "El ID del pago es obligatorio")
    private Integer pagoId;
    
    @NotBlank(message = "El concepto es obligatorio")
    @Size(max = 200, message = "El concepto no puede tener más de 200 caracteres")
    private String concepto;
    
    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser un valor positivo")
    private BigDecimal monto;
    
    private LocalDateTime fechaDetalle;
    
    @Size(max = 500, message = "Las notas no pueden tener más de 500 caracteres")
    private String notas;
}