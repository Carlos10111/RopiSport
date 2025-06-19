// src/app/features/dashboard/dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; // Importa Router
import { DashboardService } from '../../core/services/dashboard/dashboard.service';

interface DashboardMetrics {
  totalSocias: number;
  sociasActivas: number;
  nuevasSociasMes: number;
  totalEmpresas: number;
  nuevasEmpresasMes: number;
  ingresosMes: number;
  ingresosAnterior: number;
  porcentajeCrecimiento: number;
  totalPagos: number;
  pagosPendientes: number;
  pagosNoConfirmados: number;
}

interface IngresoMensual {
  mes: string;
  numeroMes: number;
  año: number;
  importe: number;
  numeroPagos: number;
}

interface AlertasDashboard {
  pagosPendientes: number;
  sociasMorosas: number;
  renovacionesVencen: number;
  pagosNoConfirmados: number;
  empresasSinActualizar: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  metricas: DashboardMetrics | null = null;
  ingresosMensuales: IngresoMensual[] = [];
  alertas: AlertasDashboard | null = null;
  loading = false;
  error = '';

  constructor(private dashboardService: DashboardService, private router: Router) {} // Inyecta Router

  ngOnInit(): void {
    this.loadDashboardData();
  }

  async loadDashboardData(): Promise<void> {
    this.loading = true;
    this.error = '';

    try {
      // Cargar métricas principales
      this.metricas = await this.dashboardService.getMetricasPrincipales();

      // Cargar ingresos mensuales (últimos 6 meses)
      this.ingresosMensuales = await this.dashboardService.getIngresosMensuales(6);

      // Cargar alertas
      this.alertas = await this.dashboardService.getAlertas();
    } catch (error) {
      this.error = 'Error al cargar los datos del dashboard';
      console.error('Error:', error);
    } finally {
      this.loading = false;
    }
  }

  getChangeIcon(porcentaje: number): string {
    return porcentaje >= 0 ? 'fas fa-arrow-up' : 'fas fa-arrow-down';
  }

  getChangeClass(porcentaje: number): string {
    return porcentaje >= 0 ? 'positive-change' : 'negative-change';
  }

  getMaxIngreso(): number {
    if (this.ingresosMensuales.length === 0) return 1;
    return Math.max(...this.ingresosMensuales.map((i) => i.importe));
  }

  // Métodos para navegar a otras rutas
  navigateToSocias(): void {
    this.router.navigate(['/admin/socias']);
  }

  /*navigateToEmpresas(): void {
    this.router.navigate(['/admin/empresa']); 
  }*/
  navigateToInstituciones(): void {
    this.router.navigate(['/admin/instituciones']); 
  }

  navigateToPagos(): void {
    this.router.navigate(['/admin/pagos']);
  }

  navigateToReportes(): void {
    this.router.navigate(['/reportes']);
  }
}