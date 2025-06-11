    export interface Socia {
      id?: number;
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
      fechaInicio?: Date | string |null;
      fechaBaja?: Date | string |null;
      observaciones?: string;
        expandido?: boolean;
       expandidoNegocio?: boolean;
       
      categoria?: {
        id: number;
        nombre: string;
      };

    }

    export interface PaginatedResponse<T> {
      content: T[];
      page: number;
      size: number;
      totalElements: number;
      totalPages: number;
      last: boolean;
    }