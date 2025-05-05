import { Component, OnInit } from '@angular/core';
import { MessageService } from '../../services/message/message.service';
import { Message } from '../../services/interfaces/message';

@Component({
  selector: 'app-message',
  imports: [],
  standalone: true,
  templateUrl: './message.component.html',
  styleUrl: './message.component.scss'
})

export class MessageComponent implements OnInit {
  mensajes: Message[] = [];
  nuevoMensaje: string = '';

  constructor(private messageService: MessageService) {}

  ngOnInit(): void {
    this.cargarMensajes();
  }

  cargarMensajes(): void {
    this.messageService.getMensajes().subscribe(data => {
      this.mensajes = data;
    });
  }
  
  enviarMensaje(): void {
    if (this.nuevoMensaje.trim()) {
      this.messageService.enviarMensaje({ contenido: this.nuevoMensaje }).subscribe(() => {
        this.nuevoMensaje = '';
        this.cargarMensajes(); // Refrescar mensajes al instante
      });
    }
  }
  
}
