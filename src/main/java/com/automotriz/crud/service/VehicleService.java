package com.automotriz.crud.service;

import com.automotriz.crud.dto.VehicleDTO;
import com.automotriz.crud.exception.ValidationServiceCustomer;

import java.util.List;
import java.util.Optional;

public interface VehicleService {
    VehicleDTO save(VehicleDTO vehicleDTO) throws ValidationServiceCustomer;

    VehicleDTO update(VehicleDTO vehicleDTO) throws ValidationServiceCustomer;

    Optional<VehicleDTO> findByLicensePlate(String licensePlate);

    List<VehicleDTO> getAllsVehicles();

    List<VehicleDTO> findByLicensePlateOrModelOrAnio(String licensePlate,String model,Integer anio);

    boolean delete(String licensePlate) throws ValidationServiceCustomer;

}
