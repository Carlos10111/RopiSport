import { Component, OnInit } from '@angular/core';
import { EventoService } from '../../services/evento/evento.service';
import { Evento } from '../../services/interfaces/evento';

@Component({
  selector: 'app-eventos',
  imports: [],
  standalone: true,
  templateUrl: './eventos.component.html',
  styleUrl: './eventos.component.scss'
})
export class EventosComponent implements OnInit {
  eventos: Evento[] = [];

  constructor(private eventoService: EventoService) {}

  ngOnInit(): void {
    this.eventoService.getEventos().subscribe(data => {
      this.eventos = data;
    });
  }
}
