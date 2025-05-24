export interface PagoDetalle {
    id: number;
    pagoId: number; // FK al pago
    concepto: string;
    monto: number;
    fechaDetalle: string; // ISO string
    notas: string;
    createdAt: string;
    updatedAt: string;
  }
  