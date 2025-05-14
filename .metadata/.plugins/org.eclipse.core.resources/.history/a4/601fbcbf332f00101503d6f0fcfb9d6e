package com.ropisport.gestion.model.dto.request;

import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeRequest {

	@NotBlank(message =  "La contraseña actual es obligatia")
	private String currentPasswardString;
	
	
	@NotBlank(message = "La nueva contraseña es obligatoria")
	@Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
	private String newPassward;
	
	@NotBlank( message = "La confirmacion de la contraseña es obligatoria")
	private String confirmPassword;
	
	
}
