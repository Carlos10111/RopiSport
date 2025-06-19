export interface SociaDTO {
    numeroSocia?: string;
    nombre: string;
    apellidos: string;
    nombreNegocio: string;
    descripcionNegocio?: string;
    direccion?: string;
    telefonoPersonal?: string;
    telefonoNegocio?: string;
    email?: string;
    cif?: string;
    numeroCuenta?: string;
    epigrafe?: string;
    activa: boolean;
    fechaInicio?: string;
    fechaBaja?: string;
    observaciones?: string;
    categoriaId?: number;
  }