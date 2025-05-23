import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Categoria } from '../../../core/models/categoria';

@Component({
  selector: 'app-categoria-form',
  templateUrl: './categoria-form.component.html',
  standalone: true,
  styleUrls: ['./categoria-form.component.scss']
})
export class CategoriaFormComponent implements OnInit {
  @Input() categoria: Categoria | null = null;
  @Output() save = new EventEmitter<Categoria>();
  form!: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      nombre: [this.categoria?.nombre || '', Validators.required]
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.save.emit({ ...this.categoria, ...this.form.value });
    }
  }
}