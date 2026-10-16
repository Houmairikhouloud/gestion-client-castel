package com.castel.client_management.repository;

import com.castel.client_management.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client, Long> {

    boolean existsByCin(String cin);
    boolean existsByCinAndIdNot(String cin, Long id);

    @Query("SELECT c FROM Client c WHERE " +
           "(:cin IS NULL OR :cin = '' OR LOWER(c.cin) LIKE LOWER(CONCAT('%', :cin, '%'))) AND " +
           "(:nomPrenom IS NULL OR :nomPrenom = '' OR " +
           " LOWER(c.nom) LIKE LOWER(CONCAT('%', :nomPrenom, '%')) OR " +
           " LOWER(c.prenom) LIKE LOWER(CONCAT('%', :nomPrenom, '%')) OR " +
           " LOWER(CONCAT(c.prenom, ' ', c.nom)) LIKE LOWER(CONCAT('%', :nomPrenom, '%'))) AND " +
           "(:agence IS NULL OR :agence = '' OR c.agence = :agence)")
    Page<Client> searchClients(
        @Param("cin") String cin,
        @Param("nomPrenom") String nomPrenom,
        @Param("agence") String agence,
        Pageable pageable
    );
}