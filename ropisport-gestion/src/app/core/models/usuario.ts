export interface Usuario {
    id: number;
    username: string;
    //passwordHash: string;
    password?: string;
    email: string;
    nombreCompleto: string;
    rolId: number;
    nombreRol: string;
    activo: boolean;
    fechaCreacion: string; // ISO string, luego se puede convertir a Date;
    ultimoAcceso: string; // ISO string
    createdAt: string; // ISO string
    updatedAt: string; // ISO string
  }