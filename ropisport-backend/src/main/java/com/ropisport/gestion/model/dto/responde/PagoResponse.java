package com.ropisport.gestion.model.dto.responde;
 import java.time.LocalDateTime;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponse {
    private Integer id;
    private SociaResponse socia;
    private Float monto;
    private LocalDateTime fechaPago;
    private String concepto;
    private String metodoPago;
    private Boolean confirmado;
}
