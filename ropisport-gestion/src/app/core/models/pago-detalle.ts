export interface PagoDetalle {
    id: number;
    pagoId: number; // FK al pago
    concepto: string;
    monto: number;
    notas: string;
    fecha_detalle: string; // ISO string
  }
  