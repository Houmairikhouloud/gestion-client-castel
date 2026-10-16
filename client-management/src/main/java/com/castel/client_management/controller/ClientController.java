package com.castel.client_management.controller;

import com.castel.client_management.dto.ClientDto;
import com.castel.client_management.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * 1. Lister les clients avec pagination et recherche multi-critères
     * Accessible par le rôle LECTEUR et le rôle GESTIONNAIRE
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('LECTEUR', 'GESTIONNAIRE')")
    public ResponseEntity<Page<ClientDto>> getClients(
            @RequestParam(required = false) String cin,
            @RequestParam(required = false) String nomPrenom,
            @RequestParam(required = false) String agence,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(clientService.searchClients(cin, nomPrenom, agence, PageRequest.of(page, size)));
    }

    /**
     * 2. Consulter les détails d'un client par son ID
     * Accessible par le rôle LECTEUR et le rôle GESTIONNAIRE
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('LECTEUR', 'GESTIONNAIRE')")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    /**
     * 3. Ajouter un nouveau client
     * Accessible UNIQUEMENT par le rôle GESTIONNAIRE
     */
    @PostMapping
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<ClientDto> createClient(@Valid @RequestBody ClientDto dto) {
        return new ResponseEntity<>(clientService.createClient(dto), HttpStatus.CREATED);
    }

    /**
     * 4. Modifier un client existant
     * Accessible UNIQUEMENT par le rôle GESTIONNAIRE
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDto dto) {
        return ResponseEntity.ok(clientService.updateClient(id, dto));
    }
}