
export interface Evento {
    id: number;
    titulo: string;
    descripcion: string;
    fecha: string; // aaaa-mm-dd (o Date)
    grupoId?: number;
  }
  