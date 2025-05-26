import { Component, inject, Input, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { EmpresaService } from '../../../core/services/empresa/empresa.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Empresa } from '../../../core/models/empresa';
import { ReactiveFormsModule } from '@angular/forms';
import { PopupService } from '../../../shared/utils/popup.service';

@Component({
  selector: 'app-empresa-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './empresa-form.component.html',
  styleUrls: ['./empresa-form.component.scss']
})
export class EmpresaFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private empresaService = inject(EmpresaService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private popup = inject(PopupService);

  @Input() empresaId?: number;

  form: FormGroup = this.fb.group({
    id: [null],
    socia_id: [null, Validators.required],
    nombre_negocio: [null, Validators.required],
    descripcion_negocio: [null],
    categoria_id: [null, Validators.required],
    direccion: [null, Validators.required],
    telefono_negocio: [null, Validators.required],
    email_negocio: [null, Validators.required],
    cif: [null, Validators.required],
    epigrafe: [null, Validators.required],
    web: [null, Validators.required],
    instagram: [null, Validators.required],
    facebook: [null, Validators.required],
    linkedin: [null, Validators.required],
    otras_redes: [null, Validators.required],
  });

  ngOnInit(): void {
    const idFromRoute = this.route.snapshot.paramMap.get('id');
    if (idFromRoute) {
      this.empresaId = +idFromRoute;
      this.loadEmpresa(this.empresaId);
    }
  }

  loadEmpresa(id: number): void {
    this.popup.loader('Cargando empresa...');
    this.empresaService.getById(id).subscribe({
      next: (empresa: Empresa) => {
        this.form.patchValue({ ...empresa });
        this.popup.close();
      },
      error: () => {
        this.popup.close();
        this.popup.showMessage('Error', 'No se pudo cargar la empresa', 'error');
      }
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.popup.showMessage('Error', 'Por favor, completa los campos obligatorios.', 'warning');
      return;
    }

    const raw = this.form.getRawValue();
    const empresa: Empresa = {
      id: raw.id ?? 0,
      socia_id: raw.socia_id!,
      nombre_negocio: raw.nombre_negocio!,
      descripcion_negocio: raw.descripcion_negocio || '',
      categoria_id: raw.categoria_id!,
      direccion: raw.direccion!,
      telefono_negocio: raw.telefono_negocio!,
      email_negocio: raw.email_negocio!,
      cif: raw.cif!,
      epigrafe: raw.epigrafe!,
      web: raw.web!,
      instagram: raw.instagram!,
      facebook: raw.facebook!,
      linkedin: raw.linkedin!,
      otras_redes: raw.otras_redes!,
    };

    this.popup.loader('Guardando empresa...');

    const request$ = this.empresaId
      ? this.empresaService.update(this.empresaId, empresa)
      : this.empresaService.create(empresa);

    request$.subscribe({
      next: () => {
        this.popup.close();
        this.popup.showMessage('Éxito', `Empresa ${this.empresaId ? 'actualizada' : 'creada'} correctamente.`, 'success');
        this.router.navigate(['/empresa']);
      },
      error: () => {
        this.popup.close();
        this.popup.showMessage('Error', `No se pudo ${this.empresaId ? 'actualizar' : 'crear'} la empresa.`, 'error');
      }
    });
  }
}