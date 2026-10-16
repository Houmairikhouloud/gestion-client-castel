package com.castel.client_management.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "client")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String cin;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    private LocalDate dateNaissance;

    @Column(nullable = false, length = 25)
    private String telephone;

    private String email;
    private String adresse;
    private String ville;
    private String codePostal;
    private String profession;

    @Column(nullable = false, length = 100)
    private String agence;

    @Builder.Default
    private String statut = "Actif";
}