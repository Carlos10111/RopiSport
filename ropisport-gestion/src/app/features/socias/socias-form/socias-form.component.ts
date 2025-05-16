import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { Socia } from '../../../core/models/socia';
import { SociaService } from '../../../core/services/socia/socia.service';
import { CategoriaNegocio } from '../../../core/models/categoriaNegocio';
import { Usuario } from '../../../core/models/usuario';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-socias-form',
  imports: [RouterLink, FormsModule],
  templateUrl: './socias-form.component.html',
  styleUrls: ['./socias-form.component.scss'],
  standalone: true,
  providers: [SociaService]
})
export class SociasFormComponent {
nuevaSocia: Socia = {
    id: 0,                            // ID único de la socia
    numeroSocia: '',
    nombre: '',
    apellidos: '',
    usuario: {} as Usuario,             // O el valor que corresponda
    nombreNegocio: '',
    descripcionNegocio: '',
    categoria: {} as CategoriaNegocio,  // O el valor que corresponda
    direccion: '',
    telefonoPersonal: '',
    telefonoNegocio: '',
    email: '',
    cif: '',
    numeroCuenta: '',
    epigrafe: '',
    activa: true,
    fechaInicio: new Date().toISOString(),
    fechaBaja: '',
    observaciones: '',
    pagos: [],
    empresas: [],
    logo: '',
  };

  constructor(private sociaService: SociaService, private router: Router) {}

  agregarSocia() {
    this.sociaService.addSocia(this.nuevaSocia);
    this.router.navigate(['/socias-list']);
  }

  onSubmit(form: any) {
    if (form.valid) {
      console.log('Formulario enviado:', this.nuevaSocia);
      // Aquí iría el código para guardar la socia
    } else {
      console.log('Formulario inválido');
    }
  }

  onLogoChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.nuevaSocia.logo = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }
}