package com.ropisport.gestion.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioRequest {
	
	@NotBlank(message = "Nombre de usuario obligatorio")
	@Size(min=6 , max=50 , message = "Nombre de usuario dene tener entre 6 y 50 caracteres ")
	private String username ;
	
	@NotBlank(message = "Contraseña obligatoria")
	@Size(min=10 , message = "Contraseña debe tener al menos 6 caracteres")
	private String password;
	
	@NotBlank(message = "Email obligatorio")
	@Email(message = "Formato email invalido")
	private String email;
	
	
	
	

}
