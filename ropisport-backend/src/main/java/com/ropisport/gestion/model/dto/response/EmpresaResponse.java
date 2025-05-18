package com.ropisport.gestion.model.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaResponse {
    private Integer id;
    private Integer sociaId;
    private String nombreSocia;
    private String nombreNegocio;
    private String descripcionNegocio;
    private Integer categoriaId;
    private String nombreCategoria;
    private String direccion;
    private String telefonoNegocio;
    private String emailNegocio;
    private String cif;
    private String epigrafe;
    private String web;
    private String instagram;
    private String facebook;
    private String linkedin;
    private String otrasRedes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}