import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { ClientService } from '../../services/client.service';
import { AuthService } from '../../services/auth.service';
import { Client } from '../../models/client.model';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './client-list.component.html',
  styleUrls: ['./client-list.component.css']
})
export class ClientListComponent implements OnInit {
  clients: Client[] = [];
  
  searchCin: string = '';
  searchNomPrenom: string = '';
  searchAgence: string = '';

  currentPage: number = 0;
  pageSize: number = 5;
  totalPages: number = 0;
  totalElements: number = 0;

  username: string = 'Mehdi HRAIDA';
  isGestionnaire: boolean = false;

  constructor(
    private clientService: ClientService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.username = this.authService.getUsername();
    this.isGestionnaire = this.authService.hasRole('GESTIONNAIRE');
    this.onSearch();
  }

  onSearch(): void {
    this.clientService.getClients(this.searchCin, this.searchNomPrenom, this.searchAgence, this.currentPage, this.pageSize)
      .subscribe({
        next: (res) => {
          this.clients = res.content;
          this.totalPages = res.totalPages;
          this.totalElements = res.totalElements;
        },
        error: (err) => console.error('Erreur chargement clients', err)
      });
  }

  onReset(): void {
    this.searchCin = '';
    this.searchNomPrenom = '';
    this.searchAgence = '';
    this.currentPage = 0;
    this.onSearch();
  }

  changePage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.onSearch();
    }
  }

  logout(): void {
    this.authService.logout();
  }
}