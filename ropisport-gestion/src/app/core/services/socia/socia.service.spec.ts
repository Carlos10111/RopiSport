import { TestBed } from '@angular/core/testing';

import { SociaService } from './socia.service';

describe('SociaService', () => {
  let service: SociaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SociaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
