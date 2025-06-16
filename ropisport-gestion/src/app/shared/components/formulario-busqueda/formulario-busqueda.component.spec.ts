import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormularioBusquedaComponent } from './formulario-busqueda.component';

describe('FormularioBusquedaComponent', () => {
  let component: FormularioBusquedaComponent;
  let fixture: ComponentFixture<FormularioBusquedaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormularioBusquedaComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormularioBusquedaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
