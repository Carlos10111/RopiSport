package com.ropisport.gestion.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolRequest {
    
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 50, message = "El nombre del rol no puede tener más de 50 caracteres")
    private String nombre;
    
    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String descripcion;
}