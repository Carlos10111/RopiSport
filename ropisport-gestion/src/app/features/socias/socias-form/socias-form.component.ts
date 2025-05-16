import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { Socia } from '../../../core/models/socia';
import { SociaService } from '../../../core/services/socia/socia.service';
@Component({
  selector: 'app-socias-form',
  imports: [RouterLink],
  templateUrl: ,
  standalone: true,
  styleUrls: []
})
export class SociasFormComponent {
  nuevaSocia: Socia = {
    numeroSocia: '',
    nombre: '',
    apellidos;
    email: '',
    telefonoPersonal: '',
    telefonoNegocio: '',
    negocio: '',
    descripcion: '',
    direccion: '',
    web: '',
    cif: '',
    numeroCuenta: '',
    epigrafe: '',
    estado: '',
    fechaInicio: '',
    fechaBaja: '',
    pagos: '',
    observaciones: '',
    logo: ''
  };

  constructor(private sociaService: SociaService, private router: Router) {}

  agregarSocia() {
    this.sociaService.addSocia(this.nuevaSocia);
    this.router.navigate(['/socias-list']);
  }
}