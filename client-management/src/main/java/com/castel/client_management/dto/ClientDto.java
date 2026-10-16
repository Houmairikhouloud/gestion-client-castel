package com.castel.client_management.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ClientDto(
    Long id,

    @NotBlank(message = "Le CIN est obligatoire")
    @Size(max = 20, message = "Le CIN ne doit pas dépasser 20 caractères")
    String cin,

    @NotBlank(message = "Le nom est obligatoire")
    String nom,

    @NotBlank(message = "Le prénom est obligatoire")
    String prenom,

    LocalDate dateNaissance,

    @NotBlank(message = "Le téléphone est obligatoire")
    String telephone,

    @Email(message = "Format d'email invalide")
    String email,

    String adresse,
    String ville,
    String codePostal,
    String profession,

    @NotBlank(message = "L'agence est obligatoire")
    String agence,

    String statut
) {}