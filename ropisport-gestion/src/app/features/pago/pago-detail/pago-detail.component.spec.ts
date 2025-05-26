import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PagoDetailComponent } from './pago-detail.component';

describe('PagoDetailComponent', () => {
  let component: PagoDetailComponent;
  let fixture: ComponentFixture<PagoDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PagoDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PagoDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
