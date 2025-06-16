import { PagoDetalleDTO } from "./pago-detalle-dto";
export interface PagoDTO {
    sociaId: number,
    monto: number,
    fechaPago: string, // o date
    concepto: string,
    metodoPago: string,
    confirmado: boolean,
    detalles: PagoDetalleDTO[],
}
