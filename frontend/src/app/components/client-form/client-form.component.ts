import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ClientService } from '../../services/client.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-client-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './client-form.component.html',
  styleUrls: ['./client-form.component.css']
})
export class ClientFormComponent implements OnInit {
  clientForm!: FormGroup;
  isEditMode = false;
  clientId?: number;
  errorMessage = '';
  username: string = 'Mehdi HRAIDA';

  constructor(
    private fb: FormBuilder,
    private clientService: ClientService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.username = this.authService.getUsername();
    this.initForm();

    this.clientId = this.route.snapshot.params['id'];
    if (this.clientId) {
      this.isEditMode = true;
      this.clientService.getClientById(this.clientId).subscribe({
        next: (c) => this.clientForm.patchValue(c),
        error: () => this.errorMessage = 'Impossible de charger le client'
      });
    }
  }

  initForm(): void {
    this.clientForm = this.fb.group({
      cin: ['', [Validators.required, Validators.maxLength(20)]],
      nom: ['', [Validators.required]],
      prenom: ['', [Validators.required]],
      dateNaissance: [''],
      telephone: ['', [Validators.required]],
      email: ['', [Validators.email]],
      adresse: [''],
      ville: [''],
      codePostal: [''],
      profession: [''],
      agence: ['', [Validators.required]]
    });
  }

  onSubmit(): void {
    if (this.clientForm.invalid) {
      this.clientForm.markAllAsTouched();
      return;
    }

    const payload = this.clientForm.value;
    const req$ = this.isEditMode
      ? this.clientService.updateClient(this.clientId!, payload)
      : this.clientService.createClient(payload);

    req$.subscribe({
      next: () => this.router.navigate(['/clients']),
      error: (err) => {
        if (err.status === 409) {
          this.errorMessage = 'Ce numéro de CIN est déjà utilisé par un autre client.';
        } else if (err.status === 403) {
          this.errorMessage = 'Action interdite : vous n\'avez pas le rôle GESTIONNAIRE.';
        } else {
          this.errorMessage = 'Une erreur est survenue lors de l\'enregistrement.';
        }
      }
    });
  }

  logout(): void {
    this.authService.logout();
  }
}