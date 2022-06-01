package com.automotriz.crud.repository;

import com.automotriz.crud.entity.Executive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ExecutiveRepository extends JpaRepository<Executive, UUID> {
    Optional<Executive> findByIdentification(String identification);

    Set<Executive> findByNumberYardCars(String numberYardCars);
}
