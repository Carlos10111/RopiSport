package com.ropisport.gestion.model.dto.request;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaRequest {
    // ✅ CAMBIO: sociaId -> sociaIds
    private List<Integer> sociaIds;
    
    @NotBlank(message = "El nombre del negocio es obligatorio")
    @Size(max = 150, message = "El nombre del negocio no puede tener más de 150 caracteres")
    private String nombreNegocio;
    
    @Size(max = 500, message = "La descripción del negocio no puede tener más de 500 caracteres")
    private String descripcionNegocio;
    
    private Integer categoriaId;
    
    @Size(max = 200, message = "La dirección no puede tener más de 200 caracteres")
    private String direccion;
    
    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    private String telefonoNegocio;
    
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String emailNegocio;
    
    @Size(max = 20, message = "El CIF no puede tener más de 20 caracteres")
    private String cif;
    
    @Size(max = 100, message = "El epígrafe no puede tener más de 100 caracteres")
    private String epigrafe;
    
    @Size(max = 200, message = "La URL web no puede tener más de 200 caracteres")
    private String web;
    
    @Size(max = 100, message = "El perfil de Instagram no puede tener más de 100 caracteres")
    private String instagram;
    
    @Size(max = 100, message = "El perfil de Facebook no puede tener más de 100 caracteres")
    private String facebook;
    
    @Size(max = 100, message = "El perfil de LinkedIn no puede tener más de 100 caracteres")
    private String linkedin;
    
    @Size(max = 500, message = "Las otras redes no pueden tener más de 500 caracteres")
    private String otrasRedes;
}