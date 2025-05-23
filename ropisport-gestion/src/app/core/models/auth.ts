import { Usuario } from "./usuario";

export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  id: number;
  username: string;
  email: string;
  nombre: string;
  apellidos: string;
  rol: string;
}




//export type LoginInterface = Pick<UserInterface, "username" | "password">
