package com.automotriz.crud.service.impl;

import com.automotriz.crud.dto.AsignationClientPatioDTO;
import com.automotriz.crud.dto.ClientDTO;
import com.automotriz.crud.dto.YardCarDTO;
import com.automotriz.crud.entity.AsignationClientPatio;
import com.automotriz.crud.entity.Client;
import com.automotriz.crud.entity.YardCar;
import com.automotriz.crud.enums.SujectCreditType;
import com.automotriz.crud.exception.ValidationServiceCustomer;
import com.automotriz.crud.repository.AsignationClientPatioRepository;
import com.automotriz.crud.service.AsignationClientPatioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AsignationClientPatioServiceImpl implements AsignationClientPatioService {

    @Autowired
    private AsignationClientPatioRepository asignationClientPatioRepository;

    @Override
    public AsignationClientPatioDTO save(AsignationClientPatioDTO asignationClientPatioDTO) throws ValidationServiceCustomer {
        asignationClientPatioDTO.setCreatedAsignation(new Date());
        Client client = buildClientDTOToClient(asignationClientPatioDTO.getClientDTO());
        YardCar yardCar = buildYardCarTDOToYardCar(asignationClientPatioDTO.getYardCarDTO());
        Optional<AsignationClientPatio> asignationClientPatioDb = asignationClientPatioRepository.findByClientAndYardCar(client, yardCar);
        if (asignationClientPatioDb.isPresent())
            throw new ValidationServiceCustomer("Ya existe una asignación del cliente " + client.getIdentification() + " con el patio de auto " + yardCar.getNumberPdv(), HttpStatus.PRECONDITION_FAILED);
        AsignationClientPatio asignationClientPatio = buildAsignationClientPatioDTOToAsignationClientPatio(asignationClientPatioDTO);
        asignationClientPatio = asignationClientPatioRepository.save(asignationClientPatio);
        return buildAsignationClientPatioToAsignationClientPatioDTO(asignationClientPatio);
    }

    @Override
    public AsignationClientPatioDTO update(AsignationClientPatioDTO asignationClientPatioDTO) throws ValidationServiceCustomer {
        if (asignationClientPatioDTO.getIdAsignation() == null)
            throw new ValidationServiceCustomer("El identificador unico de la asignacion es obligatorio para actualizar ", HttpStatus.PRECONDITION_FAILED);
        Client client = buildClientDTOToClient(asignationClientPatioDTO.getClientDTO());
        YardCar yardCar = buildYardCarTDOToYardCar(asignationClientPatioDTO.getYardCarDTO());
        Optional<AsignationClientPatio> asignationClientPatioDb = asignationClientPatioRepository.findByClientAndYardCar(client, yardCar);
        if (asignationClientPatioDb.isPresent() && (asignationClientPatioDb.get().getIdAsignation().compareTo(asignationClientPatioDTO.getIdAsignation()) != 0))
            throw new ValidationServiceCustomer("Ya existe una asignación del cliente " + client.getIdentification() + " con el patio de auto " + yardCar.getNumberPdv(), HttpStatus.PRECONDITION_FAILED);
        AsignationClientPatio asignationClientPatio = buildAsignationClientPatioDTOToAsignationClientPatio(asignationClientPatioDTO);
        asignationClientPatio = asignationClientPatioRepository.save(asignationClientPatio);
        return buildAsignationClientPatioToAsignationClientPatioDTO(asignationClientPatio);
    }

    @Override
    public boolean delete(UUID id) {
        if (id == null)
            throw new ValidationServiceCustomer("El identificador unico de la asignación es obligatorio para eliminarla ", HttpStatus.PRECONDITION_FAILED);
        Optional<AsignationClientPatio> asignationClientPatioDb = asignationClientPatioRepository.findById(id);
        if (asignationClientPatioDb.isPresent()) {
            asignationClientPatioRepository.delete(asignationClientPatioDb.get());
            return true;
        } else
            throw new ValidationServiceCustomer("Ya asignación no existe ", HttpStatus.PRECONDITION_FAILED);
    }

    @Override
    public List<AsignationClientPatioDTO> getAllAsignationClient() {
        List<AsignationClientPatio> asignationClientPatioList = asignationClientPatioRepository.findAll();
        List<AsignationClientPatioDTO> asignationClientPatioDTOList = new ArrayList<>();
        asignationClientPatioList.stream().forEach(asignationClientPatio -> {
            asignationClientPatioDTOList.add(buildAsignationClientPatioToAsignationClientPatioDTO(asignationClientPatio));
        });
        return asignationClientPatioDTOList;
    }

    private AsignationClientPatio buildAsignationClientPatioDTOToAsignationClientPatio(AsignationClientPatioDTO asignationClientPatioDTO) {
        return AsignationClientPatio.builder().idAsignation(asignationClientPatioDTO.getIdAsignation())
                .client(buildClientDTOToClient(asignationClientPatioDTO.getClientDTO()))
                .yardCar(buildYardCarTDOToYardCar(asignationClientPatioDTO.getYardCarDTO())).createdAsignation(asignationClientPatioDTO.getCreatedAsignation()).build();
    }

    private AsignationClientPatioDTO buildAsignationClientPatioToAsignationClientPatioDTO(AsignationClientPatio asignationClientPatio) {
        return AsignationClientPatioDTO.builder().idAsignation(asignationClientPatio.getIdAsignation())
                .clientDTO(buildClientToClientDTO(asignationClientPatio.getClient()))
                .yardCarDTO(buildYardCarToYardCarTDO(asignationClientPatio.getYardCar())).createdAsignation(asignationClientPatio.getCreatedAsignation()).build();
    }

    private YardCar buildYardCarTDOToYardCar(YardCarDTO yardCarDTO) {
        return YardCar.builder()
                .idCar(yardCarDTO.getIdCar())
                .name(yardCarDTO.getName())
                .address(yardCarDTO.getAddress())
                .phone(yardCarDTO.getPhone())
                .numberPdv(yardCarDTO.getNumberPdv()).build();
    }

    private Client buildClientDTOToClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setId(clientDTO.getId());
        client.setIdentification(clientDTO.getIdentification());
        client.setFirstName(clientDTO.getFirstName() != null ? clientDTO.getFirstName() : "");
        client.setLastName(clientDTO.getLastName() != null ? clientDTO.getLastName() : "");
        client.setAge(clientDTO.getAge() != null ? clientDTO.getAge() : null);
        client.setBirthDate(clientDTO.getBirthDate() != null ? clientDTO.getBirthDate() : null);
        client.setAddress(clientDTO.getAddress() != null ? clientDTO.getAddress() : "");
        client.setPhone(clientDTO.getPhone() != null ? clientDTO.getPhone() : "");
        client.setCivilStatus(clientDTO.getCivilStatus() != null ? clientDTO.getCivilStatus() : "");
        client.setIdentificationSpouse(clientDTO.getIdentificationSpouse() != null ? clientDTO.getIdentificationSpouse() : "");
        client.setSpouseName(clientDTO.getSpouseName() != null ? clientDTO.getSpouseName() : "");
        if (clientDTO.getSujectCreditType() != null && (clientDTO.getSujectCreditType().equals("Sujeto de crédito")))
            client.setSujectCreditType(SujectCreditType.SujectCredit.getName());
        else
            client.setSujectCreditType(SujectCreditType.NotSujectCredit.getName());
        return client;
    }

    private YardCarDTO buildYardCarToYardCarTDO(YardCar yardCar) {
        return YardCarDTO.builder()
                .idCar(yardCar.getIdCar())
                .name(yardCar.getName())
                .address(yardCar.getAddress())
                .phone(yardCar.getPhone())
                .numberPdv(yardCar.getNumberPdv()).build();
    }

    private ClientDTO buildClientToClientDTO(Client client) {
        return ClientDTO.builder()
                .id(client.getId())
                .identification(client.getIdentification())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .age(client.getAge())
                .birthDate(client.getBirthDate())
                .address(client.getAddress())
                .phone(client.getPhone())
                .civilStatus(client.getCivilStatus())
                .identificationSpouse(client.getIdentificationSpouse())
                .spouseName(client.getSpouseName())
                .sujectCreditType((client.getSujectCreditType().equals("Sujeto de crédito")) ? SujectCreditType.SujectCredit.getName() : SujectCreditType.NotSujectCredit.getName()).build();

    }
}
