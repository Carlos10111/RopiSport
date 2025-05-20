export interface Rol {
  id: number;
  nombre: string; //nombre: 'usuario' | 'administrador' | 'administrador_general';
  descripcion: string;
  createdAt: string; // ISO string, luego se puede convertir a Date;
  updatedAt: string;
}

