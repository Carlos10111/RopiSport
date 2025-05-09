package com.ropisport.gestion.model.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ropisport.gestion.model.audit.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categoria_negocio")
@Data
@EqualsAndHashCode(callSuper = true) 
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaNegocio extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String nombre;
    
    private String descripcion;
    
    @OneToMany(mappedBy = "categoria")
    @JsonManagedReference
    private List<Empresa> empresas;
    
    @OneToMany(mappedBy = "categoria")
    @JsonManagedReference
    private List<Socia> socias;
}