import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms'; // Si usas ngModel en el componente
import { SociasFormComponent } from './socias-form.component';
import { CommonModule } from '@angular/common'; // Importa CommonModule si lo necesitas

describe('SociasFormComponent', () => {
  let component: SociasFormComponent;
  let fixture: ComponentFixture<SociasFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SociasFormComponent],
      imports: [FormsModule] // Añade los módulos necesarios si tu componente los usa
    }).compileComponents();

    fixture = TestBed.createComponent(SociasFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
