package com.automotriz.crud.repository;

import com.automotriz.crud.entity.AsignationClientPatio;
import com.automotriz.crud.entity.Client;
import com.automotriz.crud.entity.YardCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AsignationClientPatioRepository extends JpaRepository<AsignationClientPatio, UUID> {

    Optional<AsignationClientPatio> findByClientAndYardCar(Client client, YardCar yardCar);
}
