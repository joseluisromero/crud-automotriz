package com.automotriz.crud.service;

import com.automotriz.crud.dto.YardCarDTO;
import com.automotriz.crud.exception.ValidationServiceCustomer;

import java.util.List;
import java.util.Optional;

public interface YardCarService {
    YardCarDTO save(YardCarDTO yardCarDTO) throws ValidationServiceCustomer;

    YardCarDTO update(YardCarDTO yardCarDTO) throws ValidationServiceCustomer;

    Optional<YardCarDTO> findByNumberPdv(String numberPdv);

    boolean delete(String numberPdv) throws ValidationServiceCustomer;

    List<YardCarDTO> getYardCars();
}
