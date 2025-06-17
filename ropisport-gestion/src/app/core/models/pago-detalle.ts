export interface PagoDetalle {
    id: number;
    pagoId: number; // FK al pago
    concepto: string;
    monto: number;
    fechaDetalle: Date | string; // // o date
    notas: string;
    createdAt: Date | string;
    updatedAt: Date | string;
  }
  