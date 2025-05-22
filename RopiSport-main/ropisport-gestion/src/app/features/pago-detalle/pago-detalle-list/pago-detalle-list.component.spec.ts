import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PagoDetalleListComponent } from './pago-detalle-list.component';

describe('PagoDetalleListComponent', () => {
  let component: PagoDetalleListComponent;
  let fixture: ComponentFixture<PagoDetalleListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PagoDetalleListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PagoDetalleListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
