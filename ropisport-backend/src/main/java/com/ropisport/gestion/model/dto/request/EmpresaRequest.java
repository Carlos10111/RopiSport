package com.ropisport.gestion.model.dto.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequest {
    @NotBlank(message = "El nombre del negocio es obligatorio")
    private String nombreNegocio;
    
    private String descripcionNegocio;
    
    private Integer categoriaId;
    
    private String direccion;
    
    private String telefonoNegocio;
    
    @Email(message = "Formato de email inválido")
    private String emailNegocio;
    
    private String cif;
    
    private String epigrafe;
    
    private String web;
    
    private String instagram;
    
    private String facebook;
    
    private String linkedin;
    
    private String otrasRedes;
    
    private Integer sociaId;
}