package com.ropisport.gestion.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SociaResponse {
    private Integer id;
    private String numeroSocia;
    private String nombre;
    private String apellidos;
    private Integer usuarioId;
    private String username;
    private String nombreNegocio;
    private String descripcionNegocio;
    private Integer categoriaId;
    private String nombreCategoria;
    private String direccion;
    private String telefonoPersonal;
    private String telefonoNegocio;
    private String email;
    private String cif;
    private String numeroCuenta;
    private String epigrafe;
    private Boolean activa;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaBaja;
    private String observaciones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}