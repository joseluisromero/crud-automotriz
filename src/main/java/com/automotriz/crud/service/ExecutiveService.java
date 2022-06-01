package com.automotriz.crud.service;

import com.automotriz.crud.dto.ExecutiveDTO;
import com.automotriz.crud.exception.ValidationServiceCustomer;

import java.util.List;
import java.util.Optional;

public interface ExecutiveService {
    ExecutiveDTO save(ExecutiveDTO executiveDTO) throws ValidationServiceCustomer;

    void loadExecutiveCSV();

    List<ExecutiveDTO> getExecutives();

    List<ExecutiveDTO> getExecutivesFindByNumberYardCars(String numberYardCars);

    Optional<ExecutiveDTO> findByIdentification(String identification);
}
