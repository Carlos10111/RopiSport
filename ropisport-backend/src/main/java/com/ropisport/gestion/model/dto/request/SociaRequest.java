package com.ropisport.gestion.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SociaRequest {
    
    @Size(max = 20, message = "El número de socia no puede tener más de 20 caracteres")
    private String numeroSocia;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;
    
    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden tener más de 100 caracteres")
    private String apellidos;
    
    private Integer usuarioId;
    
    @Size(max = 150, message = "El nombre del negocio no puede tener más de 150 caracteres")
    private String nombreNegocio;
    
    @Size(max = 500, message = "La descripción del negocio no puede tener más de 500 caracteres")
    private String descripcionNegocio;
    
    private Integer categoriaId;
    
    @Size(max = 200, message = "La dirección no puede tener más de 200 caracteres")
    private String direccion;
    
    @Size(max = 20, message = "El teléfono personal no puede tener más de 20 caracteres")
    private String telefonoPersonal;
    
    @Size(max = 20, message = "El teléfono del negocio no puede tener más de 20 caracteres")
    private String telefonoNegocio;
    
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;
    
    @Size(max = 20, message = "El CIF no puede tener más de 20 caracteres")
    private String cif;
    
    @Size(max = 50, message = "El número de cuenta no puede tener más de 50 caracteres")
    private String numeroCuenta;
    
    @Size(max = 100, message = "El epígrafe no puede tener más de 100 caracteres")
    private String epigrafe;
    
    private Boolean activa;
    
    private LocalDateTime fechaInicio;
    
    private LocalDateTime fechaBaja;
    
    @Size(max = 500, message = "Las observaciones no pueden tener más de 500 caracteres")
    private String observaciones;
}