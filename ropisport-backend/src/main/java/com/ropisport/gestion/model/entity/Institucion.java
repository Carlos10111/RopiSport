package com.ropisport.gestion.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ropisport.gestion.model.audit.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instituciones")
@Data
@EqualsAndHashCode(callSuper = true) 

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Institucion extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    

    @Column(name = "nombre_institucion")
    private String nombreInstitucion;
    
    @Column(name = "persona_contacto")
    private String personaContacto;
    
    
    private String cargo;
    
    private String telefono;
    
    private String email;
    
    private String web;
    
    @ManyToOne
    @JoinColumn(name = "tipo_institucion_id")
    @JsonBackReference
    private TipoInstitucion tipoInstitucion;
    
    private String observaciones;
}