package com.ropisport.gestion.model.dto.request;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolRequest {
    @NotBlank(message = "El nombre del rol es obligatorio")
    private String nombre;
    
    private String descripcion;
}
