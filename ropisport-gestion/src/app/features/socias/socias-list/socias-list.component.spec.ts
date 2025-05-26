import { ComponentFixture, TestBed } from '@angular/core/testing';


import { SociaListComponent } from './socias-list.component';
describe('SociaListComponent', () => {
  let component: SociaListComponent;
  let fixture: ComponentFixture<SociaListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SociaListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SociaListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
