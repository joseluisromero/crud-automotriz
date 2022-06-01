package com.automotriz.crud.repository;

import com.automotriz.crud.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    Optional<Vehicle> findByLicensePlate(String licensePlate);

    List<Vehicle> findByLicensePlateOrModelOrAnio(String licensePlate, String model, Integer anio);
}
