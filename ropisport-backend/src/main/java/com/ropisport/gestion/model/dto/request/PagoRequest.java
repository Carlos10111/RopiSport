package com.ropisport.gestion.model.dto.request;
import java.time.LocalDateTime;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@NotBlank
@NoArgsConstructor
@AllArgsConstructor

public class PagoRequest {
	@NotNull(message = "ID de la socia es obligatorio")
	private Integer sociaId;
	
	
	@NotNull(message = "El monto es obligatirio")
	@Min(value=0 , message = "El monot debe ser mayor o igual a 0")
	private Float monto;
	
	
	private LocalDateTime fechaPago = LocalDateTime.now();
	
	@NotBlank(message = "El concepto es obligatorio")
	private String concepto;
	
	private String metodoPagoString;
	
	private Boolean confirmadoBoolean = false;
	
	
	

}
