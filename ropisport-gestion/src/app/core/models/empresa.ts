export interface Empresa {
  id: number;
  nombreInstitucion: string;
  personaContacto: string;
  cargo: string;
  telefono: string;
  email: string;
  web: string;
  tipoInstitucionId: number; // ID del tipo de institución (relación)
  observaciones: string;
}
