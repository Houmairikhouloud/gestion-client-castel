import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Client, PageClient } from '../models/client.model';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private apiUrl = 'http://localhost:8081/api/clients';

  constructor(private http: HttpClient) {}

  getClients(cin?: string, nomPrenom?: string, agence?: string, page = 0, size = 5): Observable<PageClient> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (cin) params = params.set('cin', cin.trim());
    if (nomPrenom) params = params.set('nomPrenom', nomPrenom.trim());
    if (agence) params = params.set('agence', agence);

    return this.http.get<PageClient>(this.apiUrl, { params });
  }

  getClientById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.apiUrl}/${id}`);
  }

  createClient(client: Client): Observable<Client> {
    return this.http.post<Client>(this.apiUrl, client);
  }

  updateClient(id: number, client: Client): Observable<Client> {
    return this.http.put<Client>(`${this.apiUrl}/${id}`, client);
  }
}