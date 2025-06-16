export interface PagoDetalleDTO {
    pagoId: number,
    concepto: string,
    monto: number,
    fechaDetalle: string, // o date
    notas: string
}
