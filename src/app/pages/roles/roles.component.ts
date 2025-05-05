import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RolesService } from '../../services/roles/roles.service';
import { Rol } from '../../services/interfaces/rol';

@Component({
  selector: 'app-roles',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './roles.component.html',
  styleUrls: ['./roles.component.scss']
})
export class RolesComponent {
  roles: Rol[] = [];

  constructor(private rolesService: RolesService) {
    this.rolesService.getRoles().subscribe(data => {
      this.roles = data;
    });
  }
}
