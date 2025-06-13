// src/app/core/services/dashboard/dashboard.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface DashboardMetrics {
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

export interface IngresoMensual {
  mes: string;
  numeroMes: number;
  año: number;
  importe: number;
  numeroPagos: number;
}

export interface AlertasDashboard {
  pagosPendientes: number;
  sociasMorosas: number;
  renovacionesVencen: number;
  pagosNoConfirmados: number;
  empresasSinActualizar: number;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  // ✅ ARREGLAR: URL corregida sin /api duplicado
  private apiUrl = `${environment.apiUrl}/dashboard`; // ← Cambio aquí

  constructor(private http: HttpClient) {}

  async getMetricasPrincipales(): Promise<DashboardMetrics> {
    console.log('🔍 URL Dashboard Métricas:', `${this.apiUrl}/metricas`);
    return firstValueFrom(
      this.http.get<DashboardMetrics>(`${this.apiUrl}/metricas`)
    );
  }

  async getIngresosMensuales(meses: number = 6): Promise<IngresoMensual[]> {
    console.log('🔍 URL Dashboard Ingresos:', `${this.apiUrl}/ingresos-mensuales?meses=${meses}`);
    return firstValueFrom(
      this.http.get<IngresoMensual[]>(`${this.apiUrl}/ingresos-mensuales?meses=${meses}`)
    );
  }

  async getAlertas(): Promise<AlertasDashboard> {
    console.log('🔍 URL Dashboard Alertas:', `${this.apiUrl}/alertas`);
    return firstValueFrom(
      this.http.get<AlertasDashboard>(`${this.apiUrl}/alertas`)
    );
  }
}