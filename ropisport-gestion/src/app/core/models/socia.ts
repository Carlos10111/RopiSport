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
  fechaInicio?: Date | string | null;
  fechaBaja?: Date | string | null;
  observaciones?: string;
  expandido?: boolean;
  expandidoNegocio?: boolean;

  categoria?: {
    id: number;
    nombre: string;
  };

  empresas?: EmpresaResumen[];
}

export interface EmpresaResumen {
  id: number;
  nombreNegocio: string;
  descripcionNegocio?: string;
}
