package com.ropisport.gestion.model.dto.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SociaExcelDto {
    private Integer id;
    private String numeroSocia;
    private String nombre;             // No es username
    private String apellidos;
    private String nombreNegocio;
    private String descripcionNegocio;
    private String categoria;          // No es nombreCategoria
    private String direccion;
    private String telefonoPersonal;
    private String telefonoNegocio;
    private String email;
    private String cif;
    private String numeroCuenta;
    private String epigrafe;
    private String estado;             // No es activa (Boolean), es un String "Activa" o "Baja"
    private String fechaInicio;
    private String fechaBaja;
    private String observaciones;
    private String fechaCreacion;
    private String fechaActualizacion;
}