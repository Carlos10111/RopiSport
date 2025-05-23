export interface Rol {
  id: number;
  nombre: 'usuario' | 'administrador' | 'administrador_general';
  descripcion: string;
}