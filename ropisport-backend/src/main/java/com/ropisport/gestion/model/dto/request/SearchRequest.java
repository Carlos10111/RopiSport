package com.ropisport.gestion.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    
    @NotBlank(message = "El término de búsqueda es obligatorio")
    private String searchTerm;
}