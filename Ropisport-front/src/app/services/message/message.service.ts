import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Message } from '../interfaces/message';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private apiUrl = `${environment.apiUrl}/mensajes`;

  constructor(private http: HttpClient) {}

  getMensajes(): Observable<Message[]> {
    return this.http.get<Message[]>(this.apiUrl);
  }

  enviarMensaje(mensaje: Message): Observable<Message> {
    return this.http.post<Message>(this.apiUrl, mensaje);
  }
}
