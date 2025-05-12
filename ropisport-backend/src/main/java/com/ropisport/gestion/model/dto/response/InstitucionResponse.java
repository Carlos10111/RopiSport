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
public class InstitucionResponse {
    private Integer id;
    private String nombreInstitucion;
    private String personaContacto;
    private String cargo;
    private String telefono;
    private String email;
    private String web;
    private Integer tipoInstitucionId;
    private String nombreTipoInstitucion;
    private String observaciones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}