export interface Socia {
    id?: number;
    numeroSocia?: string;
    nombre: string;
    apellidos: string;
    //usuarioId: number;
    nombreNegocio: string;
    descripcionNegocio?: string;
    direccion?: string;
    telefonoPersonal?: string;
    telefonoNegocio?: string;
    email?: string;
    cif?: string;
    numeroCuenta?: string;
    epigrafe?: string;
    activa?: boolean;
    fechaInicio?: string; // ISO string, luego se puede convertir a Date;
    fechaBaja?: string | null;
    observaciones?: string;
    //categoria?: CategoriaDTOAlterno;
    categoria?: {
      id: number;
      nombre: string;
    };
  }

  /*export interface CategoriaDTOAlterno {
    id: number;
    nombre: string;
  }*/