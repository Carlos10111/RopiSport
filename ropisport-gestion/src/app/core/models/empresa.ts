export interface Empresa {
    id: number;
    sociaId: number;
    nombreNegocio: string;
    descripcionNegocio: string;
    categoriaId: number;
    nombreCategoria: string;
    direccion: string;
    telefonoNegocio: string;
    emailNegocio: string;
    cif: string;
    epigrafe: string;
    web: string;
    instagram: string;
    facebook: string;
    linkedin: string;
    otrasRedes: string;
    createdAt: Date | string |null;
    updatedAt: Date | string |null;
  }
  