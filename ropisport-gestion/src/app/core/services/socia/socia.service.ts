import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Socia } from '../../models/socia';
import { SociaDTO } from '../../dtos/socia-dto';
import { environment } from '../../../../environments/environment';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class SociaService {
  private sociasSubject = new BehaviorSubject<Socia[]>([]);
  socias$ = this.sociasSubject.asObservable();
  private socias: Socia[] = [];
  private apiUrl = `${environment.apiUrl}/socias`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Socia[]> {
    return this.http.get<Socia[]>(this.apiUrl);
  }

  getById(id: number): Observable<Socia> {
    return this.http.get<Socia>(`${this.apiUrl}/${id}`);
  }

  create(sociaDTO: SociaDTO): Observable<Socia> {
    return this.http.post<Socia>(this.apiUrl, sociaDTO);
  }

  update(id: number, sociaDTO: SociaDTO): Observable<Socia> {
    return this.http.put<Socia>(`${this.apiUrl}/${id}`, sociaDTO);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Agrega una nueva socia a la lista local y actualiza los subscriptores
  addSocia(socia: Socia): void {

    this.socias.push(socia);
    this.sociasSubject.next([...this.socias]);
  }

  // Emite la lista actual de socias
  getSocias(): void {
    this.sociasSubject.next([...this.socias]);
  }
}