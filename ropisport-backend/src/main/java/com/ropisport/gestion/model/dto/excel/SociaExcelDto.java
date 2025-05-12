package com.ropisport.gestion.model.dto.excel;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SociaExcelDto {
    private Integer id;
    private String numeroSocia;
    private String nombre;
    private String apellidos;
    private String username;
    private String nombreNegocio;
    private String nombreCategoria;
    private String telefonoPersonal;
    private String telefonoNegocio;
    private String email;
    private String cif;
    private Boolean activa;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaBaja;
}