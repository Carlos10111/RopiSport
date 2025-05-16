import { Rol } from './rol';
import { Socia } from './socia';

export interface Usuario {
  id: number;
  username: string;
  passwordHash: string;
  email: string;
  nombreCompleto: string;
  rol: Rol;
  activo: boolean;
  fechaCreacion: string;   // ISO date string
  ultimoAcceso: string;    // ISO date string
  socia: Socia | null;
  }
  