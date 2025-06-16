package com.ropisport.gestion.model.enums;

public enum EstadoPago {
    PENDIENTE("Pendiente"),
    PAGADO("Pagado"),
    NO_CONFIRMADO("No Confirmado"),
    VENCIDO("Vencido"),
    CANCELADO("Cancelado");
    
    private final String descripcion;
    
    EstadoPago(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}