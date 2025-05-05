import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reward } from '../interfaces/reward';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class RewardService {
  private apiUrl = `${environment.apiUrl}/rewards`;

  constructor(private http: HttpClient) {}

  getRecompensas(): Observable<Reward[]> {
    return this.http.get<Reward[]>(this.apiUrl);
  }
}
