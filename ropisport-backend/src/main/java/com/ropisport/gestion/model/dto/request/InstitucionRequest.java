package com.ropisport.gestion.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstitucionRequest {

    @NotBlank(message = "El nombre de la institución es obligatorio")
    @Size(max = 150, message = "El nombre de la institución no puede tener más de 150 caracteres")
    private String nombreInstitucion;

    @NotBlank(message = "La persona de contacto es obligatoria")
    @Size(max = 100, message = "El nombre de la persona de contacto no puede tener más de 100 caracteres")
    private String personaContacto;

    @Size(max = 100, message = "El cargo no puede tener más de 100 caracteres")
    private String cargo;

    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    private String telefono;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;

    @Size(max = 200, message = "La URL web no puede tener más de 200 caracteres")
    private String web;

    @NotNull(message = "El tipo de institución es obligatorio")
    private Integer tipoInstitucionId;

    @Size(max = 500, message = "Las observaciones no pueden tener más de 500 caracteres")
    private String observaciones;
}