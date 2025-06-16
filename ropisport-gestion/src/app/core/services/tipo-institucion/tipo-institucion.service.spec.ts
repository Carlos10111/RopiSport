import { TestBed } from '@angular/core/testing';

import { TipoInstitucionService } from './tipo-institucion.service';

describe('TipoInstitucionService', () => {
  let service: TipoInstitucionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TipoInstitucionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
