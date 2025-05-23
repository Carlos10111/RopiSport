import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TipoInstitucionFormComponent } from './tipo-institucion-form.component';

describe('TipoInstitucionFormComponent', () => {
  let component: TipoInstitucionFormComponent;
  let fixture: ComponentFixture<TipoInstitucionFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TipoInstitucionFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TipoInstitucionFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
