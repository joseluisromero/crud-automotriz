package com.automotriz.crud.repository;

import com.automotriz.crud.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    Optional<Client> findByIdentification(String identification);

    List<Client> findBySujectCreditType(String sujectCreditType);
}
