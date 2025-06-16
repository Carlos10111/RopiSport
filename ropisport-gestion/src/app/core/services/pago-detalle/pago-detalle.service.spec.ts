import { TestBed } from '@angular/core/testing';

import { PagoDetalleService } from './pago-detalle.service';

describe('PagoDetalleService', () => {
  let service: PagoDetalleService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PagoDetalleService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
