export interface PagoDetalle {
    id: number;
    pagoId: number; // FK al pago
    concepto: string;
    monto: number;
    metodoPago: string;
    fecha: string; // ISO string
  }
  