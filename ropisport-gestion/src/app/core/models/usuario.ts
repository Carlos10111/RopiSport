export interface Usuario {
    id: number;
    username: string;
    //password_hash: string;
    email: string;
    nombreCompleto: string;
    rolId: number;
    nombreRol: string;
    activo: boolean;
    fechaCreacion: Date | string;
    ultimoAcceso: string;
    createdAt: Date | string;
    updatedAt: Date | string;
  }
  