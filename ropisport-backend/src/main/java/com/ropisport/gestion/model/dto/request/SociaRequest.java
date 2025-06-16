package com.ropisport.gestion.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SociaRequest {
    private String numeroSocia;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotBlank(message = "El nombre del negocio es obligatorio")
    private String nombreNegocio;

    private String descripcionNegocio;
    private String direccion;
    private String telefonoPersonal;
    private String telefonoNegocio;

    @Email(message = "El email debe tener un formato válido")
    private String email;

    private String cif;
    private String numeroCuenta;
    private String epigrafe;

    @NotNull(message = "El estado es obligatorio")
    private Boolean activa = true;

    private String fechaInicio;
    private String fechaBaja;
    private String observaciones;

    private Integer categoriaId;
}