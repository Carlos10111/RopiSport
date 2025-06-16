export interface PagoDetalle {
    id: number;
    pagoId: number; // FK al pago
    concepto: string;
    monto: number;
    fechaDetalle: string; // // o date
    notas: string;
    createdAt: string;
    updatedAt: string;
  }
  