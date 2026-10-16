export interface Client {
  id?: number;
  cin: string;
  nom: string;
  prenom: string;
  dateNaissance?: string;
  telephone: string;
  email?: string;
  adresse?: string;
  ville?: string;
  codePostal?: string;
  profession?: string;
  agence: string;
  statut?: string;
}

export interface PageClient {
  content: Client[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}