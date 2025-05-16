import { Usuario } from "./usuario";

export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  refreshToken?: string;
  user: Usuario;
}



//export type LoginInterface = Pick<UserInterface, "username" | "password">
