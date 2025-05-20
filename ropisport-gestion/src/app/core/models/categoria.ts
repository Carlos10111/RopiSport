export interface Categoria {
    id: number;
    nombre: string;
    descripcion: string;
    createdAt: string; // ISO string, luego se puede convertir a Date;
    updatedAt: string;
    createdBy: string;
    updatedBy: string;
  }