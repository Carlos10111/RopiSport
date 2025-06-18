package com.ropisport.gestion.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ropisport.gestion.model.audit.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
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
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    // ✅ CAMBIO: Agregar relación ManyToMany
    @ManyToMany(mappedBy = "empresas")
    @JsonBackReference
    private List<Socia> socias = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}