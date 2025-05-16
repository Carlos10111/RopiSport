import { Socia } from './socia';
import { MetodoPago } from '../enum/metodoPago.enum';
import { Usuario } from './usuario'; 
export interface Pago {
  id: number;
  socia: Socia; // Relación ManyToOne
  monto: number; // BigDecimal -> number
  fechaPago: string; // LocalDateTime -> ISO string (puedes usar Date también)
  concepto?: string; // Puede ser opcional si en Java no es nullable
  metodoPago?: MetodoPago; // Enum
  confirmado?: boolean;
  pagoDetalle?: string; // Relación OneToMany (puedes usar un array de objetos si es necesario)
  // Aquí podrías añadir una lista si sabes qué entidad mapea el OneToMany final
  // por ejemplo: detalles: DetallePago[];
}
