export interface Usuario {
    id: number;
    username: string;
    //password_hash: string;
    email: string;
    nombreCompleto: string;
    rolId: number;
    nombreRol: string;
    activo: boolean;
    fechaCreacion: string;
    ultimoAcceso: string;
    createdAt: string;
    updatedAt: string;
  }
  