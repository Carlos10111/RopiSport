import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TipoInstitucionListComponent } from './tipo-institucion-list.component';

describe('TipoInstitucionListComponent', () => {
  let component: TipoInstitucionListComponent;
  let fixture: ComponentFixture<TipoInstitucionListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TipoInstitucionListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TipoInstitucionListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
