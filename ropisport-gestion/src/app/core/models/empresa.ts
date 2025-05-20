export interface Empresa {
    id: number;
    sociaId: number;
    nombreSocia: string;
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
    createdAt: string; // ISO string, luego se puede convertir a Date;
    updatedAt: string;
  }