package com.ropisport.gestion.model.dto.excel;

import lombok.Data;

@Data
public class EmpresaExcelDto {
    private Integer id;
    private String nombreSocia;
    private String nombreNegocio;
    private String nombreCategoria;
    private String telefonoNegocio;
    private String emailNegocio;
    private String cif;
    private String web;
    private String instagram;
    private String facebook;
    private String linkedin;
}