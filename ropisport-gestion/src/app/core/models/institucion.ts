export interface Institucion {
    id: number;
    nombreInstitucion: string;
    personaContacto: string;
    cargo: string;
    telefono: string;
    email: string;
    web: string;
    tipoInstitucionId: number;
    nombreTipoInstitucion: string
    observaciones: string;
    createdAt: string; // ISO string, luego se puede convertir a Date;
    updatedAt: string;
  }