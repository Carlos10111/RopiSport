import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PagoDetalleFormComponent } from './pago-detalle-form.component';

describe('PagoDetalleFormComponent', () => {
  let component: PagoDetalleFormComponent;
  let fixture: ComponentFixture<PagoDetalleFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PagoDetalleFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PagoDetalleFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
