package com.automotriz.crud.repository;

import com.automotriz.crud.entity.VehicleBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleBrandRepository extends JpaRepository<VehicleBrand, UUID> {
    Optional<VehicleBrand> findByName(String name);
}
