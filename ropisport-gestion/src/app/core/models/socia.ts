export interface Socia {        // ID único de la socia
  numeroSocia: string;
  nombre: string;
  apellidos: string;
  email: string;
  telefonoPersonal: string;
  telefonoNegocio: string;
  nombreNegocio: string;
  descripcionNegocio: string;
  direccion: string;
  web: string;
  cif: string;
  numeroCuenta: string;
  epigrafe: string;
  estado: string;
  fechaInicio: string;   // ISO string, e.g., "2025-05-16T00:00:00Z"
  fechaBaja: string | null; // Puede ser null si no hay baja
  pagos: any;            // Reemplaza 'any' por un array de tipo Pago si lo tienes definido
  observaciones: string;
  logo: string;          // Base64 o URL al logo
}