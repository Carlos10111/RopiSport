package com.ropisport.gestion.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ropisport.gestion.model.audit.Auditable;
import com.ropisport.gestion.model.enums.EstadoPago;
import com.ropisport.gestion.model.enums.MetodoPago;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pagos")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Pago extends Auditable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "socia_id", nullable = false)
    private Socia socia;
    
    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;
    
    // ALIAS para compatibilidad con el dashboard
    @Column(name = "monto", insertable = false, updatable = false)
    private BigDecimal importe; // Será mapeado al mismo campo que monto
    
    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;
    
    @Column(name = "fecha_vencimiento")
    private LocalDateTime fechaVencimiento;
    
    @Column(name = "concepto")
    private String concepto;
    
    @Column(name = "metodo_pago")
    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;
    
    @Column(name = "confirmado")
    private Boolean confirmado;
    
    // NUEVO: Estado del pago
    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoPago estado;
    
    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagoDetalle> detalles = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        if (estado == null) {
            estado = confirmado != null && confirmado ? EstadoPago.PAGADO : EstadoPago.PENDIENTE;
        }
        if (fechaVencimiento == null) {
            fechaVencimiento = fechaPago != null ? fechaPago.plusDays(30) : LocalDateTime.now().plusDays(30);
        }
        // Sincronizar importe con monto
        if (monto != null) {
            importe = monto;
        }
    }
    
    // Método helper para agregar detalles
    public void addDetalle(PagoDetalle detalle) {
        detalles.add(detalle);
        detalle.setPago(this);
    }
    
    // Método helper para remover detalles
    public void removeDetalle(PagoDetalle detalle) {
        detalles.remove(detalle);
        detalle.setPago(null);
    }
    
    // Método helper para obtener el importe (alias de monto)
    public BigDecimal getImporte() {
        return this.monto;
    }
    
    public void setImporte(BigDecimal importe) {
        this.monto = importe;
    }
}