package com.automotriz.crud.service;

import com.automotriz.crud.dto.AsignationClientPatioDTO;
import com.automotriz.crud.exception.ValidationServiceCustomer;

import java.util.List;
import java.util.UUID;

public interface AsignationClientPatioService {

    AsignationClientPatioDTO save(AsignationClientPatioDTO asignationClientPatioDTO) throws ValidationServiceCustomer;

    AsignationClientPatioDTO update(AsignationClientPatioDTO asignationClientPatioDTO) throws ValidationServiceCustomer;

    boolean delete(UUID id);

    List<AsignationClientPatioDTO> getAllAsignationClient();
}
