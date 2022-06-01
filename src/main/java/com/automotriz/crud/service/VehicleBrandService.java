package com.automotriz.crud.service;

import com.automotriz.crud.dto.VehicleBrandDTO;
import com.automotriz.crud.exception.ValidationServiceCustomer;

import java.util.List;
import java.util.Optional;

public interface VehicleBrandService {
    void loadVehicleBrandCSV();

    List<VehicleBrandDTO> getVehicleBrand();

    Optional<VehicleBrandDTO> findByName(String name);

    VehicleBrandDTO save(VehicleBrandDTO vehicleBrandDTO) throws ValidationServiceCustomer;
}
