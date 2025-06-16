import { MetodoPago } from "../enums/metodo-pago";
import { PagoDetalle } from "./pago-detalle";

export interface Pago {
  id: number;
  sociaId: number;
  nombreSocia: string;
  monto: number;
  fechaPago: string; // o date
  concepto: string;
  metodoPago: MetodoPago;
  confirmado: boolean;
  detalles: PagoDetalle [];
  createdAt: string;
  updatedAt: string;
}
