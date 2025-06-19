export interface Empresa {
  id: number;
  socias: SociaResumen[];
  nombreNegocio: string;
  descripcionNegocio?: string;
  categoriaId: number;
  nombreCategoria: string;
  direccion?: string;
  telefonoNegocio?: string;
  emailNegocio?: string;
  cif?: string;
  epigrafe?: string;
  web?: string;
  instagram?: string;
  facebook?: string;
  linkedin?: string;
  otrasRedes?: string;
  createdAt: Date | string | null;
  updatedAt: Date | string | null;
}

export interface SociaResumen {
  id: number;
  nombre: string;
  apellidos: string;
  nombreCompleto: string;
}
