import { Rol } from './rol';
import { CategoriaNegocio } from './categoriaNegocio';
import { Pago } from './pago';
import { Empresa } from './empresa';
import { Usuario } from './usuario';
export interface Socia {        // ID único de la socia
  id: number;
  numeroSocia: string;
  nombre: string;
  apellidos: string;
  usuario: Usuario;
  nombreNegocio: string;
  descripcionNegocio: string;
  categoria: CategoriaNegocio;
  direccion: string;
  telefonoPersonal: string;
  telefonoNegocio: string;
  email: string;
  cif: string;
  numeroCuenta: string;
  epigrafe: string;
  activa: boolean;
  fechaInicio: string;     // ISO date string
  fechaBaja: string;       // ISO date string
  observaciones: string;
  pagos: Pago[];
  empresas: Empresa[];
  logo: string;          // Base64 o URL al logo
}