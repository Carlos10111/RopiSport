package com.ropisport.gestion.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TipoInstitucionRequest {
	
	@NotBlank(message = "El nombre del tipo de institución es obligatorio")
	private String nombre;
	
	private String descripcion;

}
