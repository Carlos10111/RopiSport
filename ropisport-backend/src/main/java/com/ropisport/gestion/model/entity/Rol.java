package com.ropisport.gestion.model.entity;
 import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ropisport.gestion.model.audit.Auditable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
 
 
 @Entity
 @Table(name = "roles")
 @Data
 @EqualsAndHashCode(callSuper = true) 
 @NoArgsConstructor
 @AllArgsConstructor
 @Builder
 public class Rol extends Auditable {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Integer id;
     
     @Column(nullable = false, unique = true)
     private String nombre;
     
     private String descripcion;
     
     @OneToMany(mappedBy = "rol")
     @JsonManagedReference
     private List<Usuario> usuarios;
 }
