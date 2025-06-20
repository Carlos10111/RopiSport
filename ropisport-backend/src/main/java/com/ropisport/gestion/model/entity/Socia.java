package com.ropisport.gestion.model.entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ropisport.gestion.model.audit.Auditable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "socias")
@Data
@EqualsAndHashCode(callSuper = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Socia extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numero_socia", unique = true)
    private String numeroSocia;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private Usuario usuario;

    @Column(name = "nombre_negocio")
    private String nombreNegocio;

    @Column(name = "descripcion_negocio")
    private String descripcionNegocio;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonBackReference
    private CategoriaNegocio categoria;

    private String direccion;

    @Column(name = "telefono_personal")
    private String telefonoPersonal;

    @Column(name = "telefono_negocio")
    private String telefonoNegocio;

    private String email;

    private String cif;

    @Column(name = "numero_cuenta")
    private String numeroCuenta;

    private String epigrafe;

    private Boolean activa;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;

    private String observaciones;

    @Builder.Default
    @OneToMany(mappedBy = "socia", cascade = CascadeType.ALL)
    private List<Pago> pagos = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "socia", cascade = CascadeType.ALL)
    private List<Empresa> empresas = new ArrayList<>();
}