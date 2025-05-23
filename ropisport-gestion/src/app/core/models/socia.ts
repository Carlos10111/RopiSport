export interface Socia {
    id: number;
    numero_socia: string;
    nombre_apellidos: string;
    usuario_id: number;
    nombre_negocio: string;
    descripcion_negocio: string;
    categoria_id: number;
    direccion: string;
    telefono_personal: string;
    telefono_negocio: string;
    email: string;
    cif: string;
    numero_cuenta: string;
    epigrafe: string;
    activa: boolean;
    fecha_inicio: Date;
    fecha_baja: Date | null;
    observaciones: string;
  }
  