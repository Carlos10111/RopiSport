import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SociasComponent } from './socias.component';

describe('SociasComponent', () => {
  let component: SociasComponent;
  let fixture: ComponentFixture<SociasComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SociasComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SociasComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
