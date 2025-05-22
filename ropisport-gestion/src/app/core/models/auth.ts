export interface UserInterface {

  username: string;
  password: string;
  avatar: string | null;
  email: string;
  crowdcoin: number;
}


export type LoginInterface = Pick<UserInterface, "username" | "password">
//export type RegisterInterface = Omit<UserInterface,  "" >



/*export interface LoginRequest {
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

/*export interface AuthResponse {
  token: string;
  refreshToken: string;
  user: Usuario;
}*//*
export interface AuthResponse {
  token: string;
  id: number;
  username: string;
  email: string;
  rol: string;
}*/