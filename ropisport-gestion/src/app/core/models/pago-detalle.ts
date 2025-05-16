export interface PagoDetalle {
    id: number;
  pagoId: number; // Relación con el pago (foreign key)
  concepto: string;
  monto: number; // En TypeScript usamos number en lugar de BigDecimal
  fechaDetalle?: string; // ISO 8601 format e.g. "2025-05-16T12:00:00"
  notas?: string;
}
  