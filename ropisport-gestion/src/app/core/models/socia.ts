import { Categoria } from "./categoria";

export interface Socia {
    id: number;
    numeroSocia: string;
    nombre: string;
    apellidos: string;
    //usuario_id: number;
    nombreNegocio: string;
    descripcionNegocio: string;
    //categoria_id: number;
    direccion: string;
    telefonoPersonal: string;
    telefonoNegocio: string;
    email: string;
    cif: string;
    numeroCuenta: string;
    epigrafe: string;
    activa: boolean;
    fechaInicio: string;
    fechaBaja: string | null;
    observaciones: string;
    categoria: Categoria
  }
  