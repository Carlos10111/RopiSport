import { Usuario } from "./usuario";

export interface LoginRequest {
  username: string;
  password: string;
}

/*export interface RegisterRequest {
  username: string;
  password: string;
  email: string;
  nombre_completo: string;
  role_id: number;
}*/

export interface AuthResponse {
  token: string;
  refreshToken?: string;
  user: Usuario;
}



//export type LoginInterface = Pick<UserInterface, "username" | "password">
