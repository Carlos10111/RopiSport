package com.ropisport.gestion.model.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SociaRequest {
    private String numeroSocia;
    
    @NotBlank(message = "El nombre y apellidos son obligatorios")
    private String nombreApellidos;
    
    private String nombreNegocio;
    
    private String descripcionNegocio;
    
    private Integer categoriaId;
    
    private String direccion;
    
    private String telefonoPersonal;
    
    private String telefonoNegocio;
    
    @Email(message = "Formato de email inválido")
    private String email;
    
    private String cif;
    
    private String numeroCuenta;
    
    private String epigrafe;
    
    private Boolean activa = true;
    
    private String observaciones;
    
    private Integer usuarioId;
}