import { Institucion } from "./institucion";
export interface TipoInstitucion {
     id: number;
  nombre: string;
  descripcion: string;
  instituciones: Institucion[]; // Relación uno a muchos
}