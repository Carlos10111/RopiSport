export interface Usuario {
    id: number;
    username: string;
    password_hash: string;
    email: string;
    nombre_completo: string;
    role_id: number;
    activo: boolean;
    fecha_creacion: Date;
    ultimo_acceso: Date;
  }
  