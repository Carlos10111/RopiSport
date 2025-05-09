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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "empresas")
@Data
@EqualsAndHashCode(callSuper = true) 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @OneToOne
    @JoinColumn(name = "socia_id")
    @JsonBackReference
    private Socia socia;
    
    @Column(name = "nombre_negocio")
    private String nombreNegocio;
    
    @Column(name = "descripcion_negocio")
    private String descripcionNegocio;
    
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonBackReference
    private CategoriaNegocio categoria;
    
    private String direccion;
    
    @Column(name = "telefono_negocio")
    private String telefonoNegocio;
    
    @Column(name = "email_negocio")
    private String emailNegocio;
    
    private String cif;
    
    private String epigrafe;
    
    private String web;
    
    private String instagram;
    
    private String facebook;
    
    private String linkedin;
    
    @Column(name = "otras_redes")
    private String otrasRedes;
}