package com.ropisport.gestion.model.dto.response;

import java.time.LocalDateTime;
import java.util.List;

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
    
    // ✅ CAMBIO: sociaId/nombreSocia -> socias
    private List<SociaDto> socias;
    
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
    
    // ✅ AÑADIR: DTO para socias
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SociaDto {
        private Integer id;
        private String nombre;
        private String apellidos;
        private String nombreCompleto;
    }
}