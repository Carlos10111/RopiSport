package com.ropisport.gestion.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SociaResponse {
    private Integer id;
    private String numeroSocia;
    private String nombre;
    private String apellidos;
    private String nombreNegocio;
    private String descripcionNegocio;
    private String direccion;
    private String telefonoPersonal;
    private String telefonoNegocio;
    private String email;
    private String cif;
    private String numeroCuenta;
    private String epigrafe;
    private Boolean activa;
    private String fechaInicio;
    private String fechaBaja;
    private String observaciones;
    private CategoriaDto categoria;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoriaDto {
        private Integer id;
        private String nombre;
    }
}