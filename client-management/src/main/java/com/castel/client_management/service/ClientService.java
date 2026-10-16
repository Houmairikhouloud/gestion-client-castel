package com.castel.client_management.service;

import com.castel.client_management.dto.ClientDto;
import com.castel.client_management.entity.Client;
import com.castel.client_management.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public Page<ClientDto> searchClients(String cin, String nomPrenom, String agence, Pageable pageable) {
        return repository.searchClients(cin, nomPrenom, agence, pageable).map(this::toDto);
    }

    public ClientDto getClientById(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client non trouvé"));
    }

    public ClientDto createClient(ClientDto dto) {
        if (repository.existsByCin(dto.cin())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un client avec ce CIN existe déjà.");
        }
        Client client = toEntity(dto);
        client.setStatut("Actif");
        return toDto(repository.save(client));
    }

    public ClientDto updateClient(Long id, ClientDto dto) {
        Client existing = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client non trouvé"));

        if (repository.existsByCinAndIdNot(dto.cin(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un autre client utilise déjà ce CIN.");
        }

        existing.setCin(dto.cin());
        existing.setNom(dto.nom());
        existing.setPrenom(dto.prenom());
        existing.setDateNaissance(dto.dateNaissance());
        existing.setTelephone(dto.telephone());
        existing.setEmail(dto.email());
        existing.setAdresse(dto.adresse());
        existing.setVille(dto.ville());
        existing.setCodePostal(dto.codePostal());
        existing.setProfession(dto.profession());
        existing.setAgence(dto.agence());

        return toDto(repository.save(existing));
    }

    private ClientDto toDto(Client c) {
        return new ClientDto(
            c.getId(), c.getCin(), c.getNom(), c.getPrenom(), c.getDateNaissance(),
            c.getTelephone(), c.getEmail(), c.getAdresse(), c.getVille(),
            c.getCodePostal(), c.getProfession(), c.getAgence(), c.getStatut()
        );
    }

    private Client toEntity(ClientDto d) {
        return Client.builder()
            .cin(d.cin())
            .nom(d.nom())
            .prenom(d.prenom())
            .dateNaissance(d.dateNaissance())
            .telephone(d.telephone())
            .email(d.email())
            .adresse(d.adresse())
            .ville(d.ville())
            .codePostal(d.codePostal())
            .profession(d.profession())
            .agence(d.agence())
            .statut(d.statut() != null ? d.statut() : "Actif")
            .build();
    }
}