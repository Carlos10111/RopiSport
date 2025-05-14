package com.ropisport.gestion.model.entity;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ropisport.gestion.model.audit.Auditable;
import com.ropisport.gestion.model.enums.MetodoPago;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pagos")
@Data
@EqualsAndHashCode(callSuper = true) 

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "socia_id")
    @JsonBackReference
    private Socia socia;
    
    private Float monto;
    
    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;
    
    private String concepto;
    
    @Column(name = "metodo_pago")
    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;
    
    private Boolean confirmado;
}