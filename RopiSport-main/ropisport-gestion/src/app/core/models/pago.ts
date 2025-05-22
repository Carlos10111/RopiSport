export interface Pago {
  id: number;
  socia_id: number;
  monto: number;
  fecha_pago: Date; // ISO 8601
  concepto: string;
  metodo_pago: string;
  confirmado: boolean;
}
