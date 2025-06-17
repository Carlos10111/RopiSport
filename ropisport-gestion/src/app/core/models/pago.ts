import { MetodoPago } from "../enums/metodo-pago";
import { PagoDetalle } from "./pago-detalle";

export interface Pago {
  id: number;
  sociaId: number;
  nombreSocia: string;
  monto: number;
  fechaPago: Date | string; // o date
  concepto: string;
  metodoPago: MetodoPago;
  confirmado: boolean;
  detalles: PagoDetalle [];
  createdAt: Date | string;
  updatedAt: Date | string;
}
