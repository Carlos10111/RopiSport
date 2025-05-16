import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SociasListComponent } from './socias-list.component';

describe('SociasListComponent', () => {
  let component: SociasListComponent;
  let fixture: ComponentFixture<SociasListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SociasListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SociasListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
