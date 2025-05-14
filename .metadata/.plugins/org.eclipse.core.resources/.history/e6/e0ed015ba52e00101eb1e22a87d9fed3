package com.ropisport.gestion.model.dto.responde;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Integer id;
    private String username;
    private String email;
    private String nombreCompleto;
    private String rol;
    
    public JwtResponse(String token, Integer id, String username, String email, String nombreCompleto, String rol) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }
}