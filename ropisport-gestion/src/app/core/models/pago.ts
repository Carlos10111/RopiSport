import { MetodoPago } from "../enums/metodo-pago";
import { PagoDetalle } from "./pago-detalle";

export interface Pago {
  id: number;
  sociaId: number;
  nombreSocia: string;
  monto: number;
  fechaPago: string; // ISO 8601
  concepto: string;
  metodoPago: MetodoPago;
  confirmado: boolean;
  detalles: PagoDetalle [];
  createdAt: string;
  updatedAt: string;
}
