package com.automotriz.crud.service;

import com.automotriz.crud.dto.ClientDTO;
import com.automotriz.crud.exception.ValidationServiceCustomer;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<ClientDTO> getClientsByTypeClient(String typeClient);

    List<ClientDTO> getClients();

    Optional<ClientDTO> findByIdentification(String identification);

    ClientDTO save(ClientDTO clientDTO) throws ValidationServiceCustomer;

    ClientDTO update(ClientDTO clientDTO) throws ValidationServiceCustomer;

    boolean delete(String identification) throws ValidationServiceCustomer;

    void loadClientCSV();
}
