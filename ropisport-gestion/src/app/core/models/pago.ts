import { MetodoPago } from "../enums/metodo-pago-enum";
import { PagoDetalle } from "./pago-detalle";

export interface Pago {
  id: number;
  sociaId: number;
  monto: number;
  fechaPago: string; // ISO string
  concepto: string;
  metodoPago: MetodoPago;
  confirmado: boolean;
  detalles: PagoDetalle;
  createdAt: string; // ISO string, luego se puede convertir a Date;
  updatedAt: string;
}
