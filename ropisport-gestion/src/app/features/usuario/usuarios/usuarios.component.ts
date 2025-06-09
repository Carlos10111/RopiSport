import { Component, OnInit } from '@angular/core';
import { Usuario } from '../../../core/models/usuario';
import { UsuarioDTO } from '../../../core/dtos/usuario-dto';
import { Rol } from '../../../core/models/rol';
import { CredentialsService } from '../../../core/auth/credentials.service';
import { UsuarioService } from '../../../core/services/usuario/usuario.service';
import { PaginatedResponse } from '../../../core/models/paginated-response';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.scss']
})
export class UsuariosComponent implements OnInit {
  // Variables para las pestañas
  activeTab: 'usuarios' | 'crear' = 'usuarios';

  // Variables para la tabla de usuarios
  usuarios: Usuario[] = [];
  loading = false;
  error = '';
  success = '';
  
  // Variables para paginación
  currentPage = 0;
  totalPages = 0;
  pageSize = 10;
  
  // Variables para búsqueda
  filtroActual = '';
  
  // Variables para modales
  modalActivo: 'buscarModificar' | 'buscarEliminar' | 'editar' | 'eliminar' | '' = '';
  terminoBusqueda = '';
  resultadosBusqueda: Usuario[] = [];
  buscando = false;
  guardando = false;
  
  // Variables para usuario actual
  usuarioActual: Usuario = this.getEmptyUsuario();
  
  // Variables para crear usuario
  nuevoUsuario: UsuarioDTO = this.getEmptyUsuarioDTO();
  confirmPassword = '';
  emailInvalido = false;
  
  // Variables para roles
  roles: Rol[] = [];

  constructor(
    private usuarioService: UsuarioService,
    private credentialsService: CredentialsService
  ) {}

  ngOnInit(): void {
    this.cargarUsuarios();
    this.cargarRoles();
  }

  // Métodos para las pestañas
  setActiveTab(tab: 'usuarios' | 'crear'): void {
    this.activeTab = tab;
    this.limpiarAlertas();
    if (tab === 'usuarios') {
      this.cargarUsuarios();
    } else {
      this.limpiarFormulario();
    }
  }

  // Métodos para cargar datos
  cargarUsuarios(page: number = 0, filtro: string = ''): void {
    this.loading = true;
    this.error = '';
    
    if (filtro) {
      // Usar búsqueda general si hay filtro
      this.usuarioService.busquedaGeneral(filtro, undefined, undefined, page, this.pageSize)
        .subscribe({
          next: (response: PaginatedResponse<Usuario>) => {
            this.usuarios = response.content;
            this.currentPage = response.page;
            this.totalPages = response.totalPages;
            this.loading = false;
          },
          error: (error) => {
            console.error('Error al cargar usuarios:', error);
            this.error = 'Error al cargar los usuarios';
            this.loading = false;
          }
        });
    } else {
      // Cargar todos los usuarios
      this.usuarioService.getAllUsuarios(page, this.pageSize)
        .subscribe({
          next: (response: PaginatedResponse<Usuario>) => {
            this.usuarios = response.content;
            this.currentPage = response.page;
            this.totalPages = response.totalPages;
            this.loading = false;
          },
          error: (error) => {
            console.error('Error al cargar usuarios:', error);
            this.error = 'Error al cargar los usuarios';
            this.loading = false;
          }
        });
    }
  }

  cargarRoles(): void {
    this.usuarioService.getRoles()
      .subscribe({
        next: (roles) => {
          this.roles = roles;
        },
        error: (error) => {
          console.error('Error al cargar roles:', error);
        }
      });
  }

  // Métodos de búsqueda
  onSearch(event: any): void {
    const filtro = event.target.value;
    this.filtroActual = filtro;
    this.cargarUsuarios(0, filtro);
  }

  buscarUsuarios(): void {
    if (!this.terminoBusqueda || this.terminoBusqueda.length < 2) {
      this.resultadosBusqueda = [];
      return;
    }

    this.buscando = true;
    
    // Usar búsqueda general con tamaño grande para obtener varios resultados
    this.usuarioService.busquedaGeneral(this.terminoBusqueda, undefined, undefined, 0, 50)
      .subscribe({
        next: (response: PaginatedResponse<Usuario>) => {
          this.resultadosBusqueda = response.content;
          this.buscando = false;
        },
        error: (error) => {
          console.error('Error en búsqueda:', error);
          this.resultadosBusqueda = [];
          this.buscando = false;
        }
      });
  }

  // Métodos de paginación
  onPageChange(page: number): void {
    this.cargarUsuarios(page, this.filtroActual);
  }

  // Métodos para crear usuario
  crearUsuario(): void {
    this.limpiarAlertas();
    
    // Validaciones
    if (!this.validarFormularioCreacion()) {
      return;
    }

    this.guardando = true;
    
    // Usar el servicio de usuarios en lugar de credentials
    this.usuarioService.createUsuario(this.nuevoUsuario)
      .subscribe({
        next: (response) => {
          this.success = 'Usuario creado exitosamente';
          this.limpiarFormulario();
          this.guardando = false;
          // Recargar usuarios si estamos en esa pestaña
          if (this.activeTab === 'usuarios') {
            this.cargarUsuarios();
          }
        },
        error: (error) => {
          console.error('Error al crear usuario:', error);
          this.error = error.error?.message || 'Error al crear el usuario';
          this.guardando = false;
        }
      });
  }

  validarFormularioCreacion(): boolean {
    // Validar email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    this.emailInvalido = !emailRegex.test(this.nuevoUsuario.email);
    
    // Validaciones básicas
    if (!this.nuevoUsuario.username || 
        !this.nuevoUsuario.email || 
        !this.nuevoUsuario.password || 
        !this.nuevoUsuario.nombreCompleto || 
        !this.nuevoUsuario.rolId ||
        this.emailInvalido ||
        this.nuevoUsuario.password !== this.confirmPassword ||
        this.nuevoUsuario.password.length < 6) {
      return false;
    }
    
    return true;
  }

  limpiarFormulario(): void {
    this.nuevoUsuario = this.getEmptyUsuarioDTO();
    this.confirmPassword = '';
    this.emailInvalido = false;
    this.limpiarAlertas();
  }

  // Métodos para modales
  abrirModalBuscarParaModificar(): void {
    this.modalActivo = 'buscarModificar';
    this.terminoBusqueda = '';
    this.resultadosBusqueda = [];
  }

  abrirModalBuscarParaEliminar(): void {
    this.modalActivo = 'buscarEliminar';
    this.terminoBusqueda = '';
    this.resultadosBusqueda = [];
  }

  seleccionarUsuarioParaEditar(usuario: Usuario): void {
    this.usuarioActual = { ...usuario };
    this.modalActivo = 'editar';
  }

  seleccionarUsuarioParaEliminar(usuario: Usuario): void {
    this.usuarioActual = { ...usuario };
    this.modalActivo = 'eliminar';
  }

  cerrarModal(): void {
    this.modalActivo = '';
    this.terminoBusqueda = '';
    this.resultadosBusqueda = [];
    this.usuarioActual = this.getEmptyUsuario();
  }

  // Métodos para editar usuario
  guardarEdicionUsuario(): void {
    this.limpiarAlertas();
    this.guardando = true;
    
    const usuarioEditar: UsuarioDTO = {
      username: this.usuarioActual.username,
      email: this.usuarioActual.email,
      nombreCompleto: this.usuarioActual.nombreCompleto,
      rolId: this.usuarioActual.rolId,
      activo: this.usuarioActual.activo,
      password: '' // No enviamos password en edición
    };
    
    this.usuarioService.updateUsuario(this.usuarioActual.id, usuarioEditar)
      .subscribe({
        next: () => {
          this.success = 'Usuario actualizado exitosamente';
          this.cerrarModal();
          this.cargarUsuarios(this.currentPage, this.filtroActual);
          this.guardando = false;
        },
        error: (error) => {
          console.error('Error al actualizar usuario:', error);
          this.error = error.error?.message || 'Error al actualizar el usuario';
          this.guardando = false;
        }
      });
  }

  // Métodos para eliminar usuario
  confirmarEliminar(): void {
    this.limpiarAlertas();
    this.guardando = true;
    
    this.usuarioService.deleteUsuario(this.usuarioActual.id)
      .subscribe({
        next: () => {
          this.success = 'Usuario eliminado exitosamente';
          this.cerrarModal();
          this.cargarUsuarios(this.currentPage, this.filtroActual);
          this.guardando = false;
        },
        error: (error) => {
          console.error('Error al eliminar usuario:', error);
          this.error = error.error?.message || 'Error al eliminar el usuario';
          this.guardando = false;
        }
      });
  }

  // Métodos utilitarios
  getEmptyUsuario(): Usuario {
    return {
      id: 0,
      username: '',
      email: '',
      nombreCompleto: '',
      rolId: 0,
      nombreRol: '',
      activo: true,
      fechaCreacion: '',
      ultimoAcceso: '',
      createdAt: '',
      updatedAt: ''
    };
  }

  getEmptyUsuarioDTO(): UsuarioDTO {
    return {
      username: '',
      password: '',
      email: '',
      nombreCompleto: '',
      rolId: 0,
      activo: true
    };
  }

  limpiarAlertas(): void {
    this.error = '';
    this.success = '';
  }
  
}
