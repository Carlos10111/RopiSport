export interface PagoDetalle {
    id: number;
    pagoId: number; // FK al pago
    concepto: string;
    monto: number;
    fechaDetalle: string; // ISO string, luego se puede convertir a Date
    notas: string;
    createdAt: string; // ISO string
    updatedAt: string;
  }