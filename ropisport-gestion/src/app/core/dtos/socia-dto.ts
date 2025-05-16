export interface SociaDTO {
    numeroSocia: string;
    nombreApellidos: string;
    usuarioId: number;
    nombreNegocio: string;
    descripcionNegocio: string;
    categoriaId: number;
    direccion: string;
    telefonoPersonal: string;
    telefonoNegocio: string;
    email: string;
    cif: string;
    numeroCuenta: string;
    epigrafe: string;
    activa: boolean;
    fechaInicio: Date;
    fechaBaja: Date | null;
    observaciones: string;
}
