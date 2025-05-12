import { Component, inject, Input, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { InstitucionService } from '../../../core/services/institucion/institucion.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Institucion } from '../../../core/models/institucion';
import { ReactiveFormsModule } from '@angular/forms';
import { TipoInstitucionService } from '../../../core/services/tipo-institucion/tipo-institucion.service';
import { TipoInstitucion } from '../../../core/models/tipo-institucion';
import { BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-institucion-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './institucion-form.component.html',
  styleUrls: ['./institucion-form.component.scss']
})
export class InstitucionFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private institucionService = inject(InstitucionService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private tipoInstitucionService = inject(TipoInstitucionService);

  // Input para recibir un id de la institución si estamos actualizando
  @Input() institucionId?: number;

  form: FormGroup;

  tiposInstitucion$ = new BehaviorSubject<TipoInstitucion[]>([]);

  constructor() {
    this.form = this.fb.group({
      id: [null],
      persona_contacto: ['', Validators.required],
      nombre_institucion: ['', Validators.required],
      cargo: ['', Validators.required],
      telefono: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      web: ['', Validators.required],
      tipo_institucion_id: [null, Validators.required],
      observaciones: ['']
    });
  }

  ngOnInit(): void {
    if (this.institucionId) {
      this.loadInstitucion(this.institucionId);
    }

    this.tipoInstitucionService.getAll().subscribe(data => {
      this.tiposInstitucion$.next(data);
    });
  }

  loadInstitucion(id: number): void {
    this.institucionService.getById(id).subscribe({
      next: (institucion: Institucion) => {
        this.form.patchValue(institucion); // Llenar el formulario con los datos
      },
      error: () => {
        console.error('Error al cargar la institución');
      }
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      console.log('Formulario inválido');
      return;
    }

    const raw = this.form.getRawValue();
    const institucion: Institucion = {
      id: raw.id ?? 0,
      persona_contacto: raw.persona_contacto!,
      nombre_institucion: raw.nombre_institucion!,
      cargo: raw.cargo!,
      telefono: raw.telefono!,
      email: raw.email!,
      web: raw.web!,
      tipo_institucion_id: raw.tipo_institucion_id!,
      observaciones: raw.observaciones || ''
    };

    if (institucion.id) {
      // Si tiene id, es una actualización
      this.institucionService.update(institucion.id, institucion).subscribe({
        next: () => {
          this.router.navigate(['/instituciones']); // Redirigir después
        },
        error: () => {
          console.error('Error al actualizar la institución');
        }
      });
    } else {
      // Si no tiene id, es una creación
      this.institucionService.create(institucion).subscribe({
        next: () => {
          this.router.navigate(['/instituciones']);
        },
        error: () => {
          console.error('Error al crear la institución');
        }
      });
    }
  }
}


/*import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { InstitucionService } from '../../../core/services/institucion/institucion.service';
import { TipoInstitucionService } from '../../../core/services/tipo-institucion/tipo-institucion.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Institucion } from '../../../core/models/institucion';
import { TipoInstitucion } from '../../../core/models/tipo-institucion';
import { BehaviorSubject } from 'rxjs';
import { PopupService } from '../../../shared/utils/popup.service';

@Component({
  selector: 'app-institucion-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './institucion-form.component.html',
})
export class InstitucionFormComponent implements OnInit {
  private fb = inject(FormBuilder);
  private institucionService = inject(InstitucionService);
  private tipoInstitucionService = inject(TipoInstitucionService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private popup = inject(PopupService);

  tiposInstitucion$ = new BehaviorSubject<TipoInstitucion[]>([]);
  form: FormGroup = this.fb.group({
    id: [null],
    nombre: ['', Validators.required],
    tipoInstitucionId: [null, Validators.required],
  });

  institucionId?: number;

  ngOnInit(): void {
    const idFromRoute = this.route.snapshot.paramMap.get('id');
    if (idFromRoute) {
      this.institucionId = +idFromRoute;
      this.loadInstitucion(this.institucionId);
    }

    this.tipoInstitucionService.getAll().subscribe({
      next: tipos => this.tiposInstitucion$.next(tipos),
      error: () => this.popup.showMessage('Error', 'No se pudieron cargar los tipos', 'error'),
    });
  }

  loadInstitucion(id: number): void {
    this.popup.loader('Cargando institución...');
    this.institucionService.getById(id).subscribe({
      next: data => {
        this.form.patchValue(data);
        this.popup.close();
      },
      error: () => {
        this.popup.close();
        this.popup.showMessage('Error', 'No se pudo cargar la institución', 'error');
      },
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.popup.showMessage('Error', 'Por favor, completa los campos obligatorios.', 'warning');
      return;
    }

    const data: Institucion = this.form.getRawValue();

    this.popup.loader('Guardando institución...');

    const request$ = this.institucionId
      ? this.institucionService.update(this.institucionId, data)
      : this.institucionService.create(data);

    request$.subscribe({
      next: () => {
        this.popup.close();
        this.popup.showMessage('Éxito', `Institución ${this.institucionId ? 'actualizada' : 'creada'} correctamente.`, 'success');
        this.router.navigate(['/institucion']);
      },
      error: () => {
        this.popup.close();
        this.popup.showMessage('Error', `No se pudo ${this.institucionId ? 'actualizar' : 'crear'} la institución.`, 'error');
      },
    });
  }
}*/
