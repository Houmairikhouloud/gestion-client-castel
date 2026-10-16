import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private keycloak!: Keycloak;

  init(): Promise<boolean> {
    this.keycloak = new Keycloak({
      url: 'http://localhost:8080',
      realm: 'castel-realm',
      clientId: 'castel-frontend'
    });

    return this.keycloak.init({
      onLoad: 'login-required',
      checkLoginIframe: false,
      pkceMethod: 'S256'
    });
  }

  getToken(): string | undefined {
    return this.keycloak?.token;
  }

  getUsername(): string {
    return this.keycloak?.tokenParsed?.['preferred_username'] || 'Mehdi HRAIDA';
  }

  hasRole(role: string): boolean {
    const realmRoles = this.keycloak?.tokenParsed?.['realm_access']?.roles || [];
    return realmRoles.includes(role);
  }

  logout(): void {
    this.keycloak.logout({ redirectUri: window.location.origin });
  }
}