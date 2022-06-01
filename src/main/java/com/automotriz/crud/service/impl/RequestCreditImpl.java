package com.automotriz.crud.service.impl;

import com.automotriz.crud.dto.*;
import com.automotriz.crud.entity.*;
import com.automotriz.crud.enums.SujectCreditType;
import com.automotriz.crud.exception.ValidationServiceCustomer;
import com.automotriz.crud.repository.AsignationClientPatioRepository;
import com.automotriz.crud.repository.RequestCreditRepository;
import com.automotriz.crud.service.RequestCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RequestCreditImpl implements RequestCreditService {

    @Autowired
    private RequestCreditRepository requestCreditRepository;
    @Autowired
    private AsignationClientPatioRepository asignationClientPatioRepository;

    @Transactional
    @Override
    public RequestCreditDTO save(RequestCreditDTO requestCreditDTO) throws ValidationServiceCustomer {
        requestCreditDTO.setDateElaboration(new Date());
        Client client = buildClientDTOToClient(requestCreditDTO.getClientDTO());
        Vehicle vehicle = buildVehicleDTOToVehicle(requestCreditDTO.getVehicleDTO());
        //Executive executive = buildVehicleBrandDTOToExecutive(requestCreditDTO.getExecutiveDTO());
        YardCar yardCar = buildYardCarTDOToYardCar(requestCreditDTO.getYardCarDTO());
        Optional<RequestCredit> requestCreditClientDb = requestCreditRepository.findByClientAndStatusAndDateElaboration(client, requestCreditDTO.getStatus(), new Date());
        if (requestCreditClientDb.isPresent())
            throw new ValidationServiceCustomer("El cliente " + client.getIdentification() + " ya tiene una solicitud el dia de hoy", HttpStatus.PRECONDITION_FAILED);

        Optional<RequestCredit> requestCreditvehicleDb = requestCreditRepository.findByVehicleAndStatus(vehicle, requestCreditDTO.getStatus());
        if (requestCreditvehicleDb.isPresent())
            throw new ValidationServiceCustomer("El vehiculo placa: " + vehicle.getLicensePlate() + " ya se encuentra en reserva", HttpStatus.PRECONDITION_FAILED);

        Optional<AsignationClientPatio> asignationClientPatioDb = asignationClientPatioRepository.findByClientAndYardCar(client, yardCar);

        RequestCredit requestCredit = buildRequestCreditTDOToRequestCredit(requestCreditDTO);
        requestCredit = requestCreditRepository.save(requestCredit);
        if (requestCredit.getIdRequestCredit() != null && !asignationClientPatioDb.isPresent()) {
            AsignationClientPatio asignationClientPatio = buildAsignationClientPatio(client, yardCar);
            asignationClientPatio = asignationClientPatioRepository.save(asignationClientPatio);
        }
        return buildRequestCreditToRequestCreditTDO(requestCredit);
    }

    @Transactional
    @Override
    public RequestCreditDTO update(RequestCreditDTO requestCreditDTO) throws ValidationServiceCustomer {
        if (requestCreditDTO.getIdRequestCredit() == null)
            throw new ValidationServiceCustomer("El id de la solicitud es obligatorio para la actualización", HttpStatus.PRECONDITION_FAILED);

        Client client = buildClientDTOToClient(requestCreditDTO.getClientDTO());
        Vehicle vehicle = buildVehicleDTOToVehicle(requestCreditDTO.getVehicleDTO());
        //Executive executive = buildVehicleBrandDTOToExecutive(requestCreditDTO.getExecutiveDTO());
        YardCar yardCar = buildYardCarTDOToYardCar(requestCreditDTO.getYardCarDTO());
        Optional<RequestCredit> requestCreditClientDb = requestCreditRepository.findById(requestCreditDTO.getIdRequestCredit());
        if (requestCreditClientDb.isPresent()) {

            Optional<AsignationClientPatio> asignationClientPatioDb = asignationClientPatioRepository.findByClientAndYardCar(client, yardCar);

            RequestCredit requestCredit = buildRequestCreditTDOToRequestCredit(requestCreditDTO);
            requestCredit = requestCreditRepository.save(requestCredit);
            if (!asignationClientPatioDb.isPresent()) {
                AsignationClientPatio asignationClientPatio = buildAsignationClientPatio(client, yardCar);
                asignationClientPatio = asignationClientPatioRepository.save(asignationClientPatio);
            }
            return buildRequestCreditToRequestCreditTDO(requestCredit);
        } else {
            throw new ValidationServiceCustomer("La solicitud de credito no se encuentra registrada", HttpStatus.PRECONDITION_FAILED);
        }

    }

    @Override
    public List<RequestCreditDTO> getRequestCreditAll() {
        List<RequestCredit> requestCreditList = requestCreditRepository.findAll();
        List<RequestCreditDTO> requestCreditDTOList = new ArrayList<>();
        requestCreditList.stream().forEach(requestCredit -> {
            requestCreditDTOList.add(buildRequestCreditToRequestCreditTDO(requestCredit));
        });
        return requestCreditDTOList;
    }

    @Override
    public List<RequestCreditDTO> getRequestByStatus(String status) {
        if (status == null)
            throw new ValidationServiceCustomer("El campo de busqueda es obligatorio", HttpStatus.PRECONDITION_FAILED);
        List<RequestCredit> requestCreditList = requestCreditRepository.findByStatus(status);
        List<RequestCreditDTO> requestCreditDTOList = new ArrayList<>();
        requestCreditList.stream().forEach(requestCredit -> {
            requestCreditDTOList.add(buildRequestCreditToRequestCreditTDO(requestCredit));
        });
        return requestCreditDTOList;
    }

    private RequestCredit buildRequestCreditTDOToRequestCredit(RequestCreditDTO requestCreditDTO) {
        return RequestCredit.builder()
                .idRequestCredit(requestCreditDTO.getIdRequestCredit())
                .dateElaboration(requestCreditDTO.getDateElaboration())
                .monthsTerm(requestCreditDTO.getMonthsTerm())
                .entry(requestCreditDTO.getEntry())
                .quotas(requestCreditDTO.getQuotas())
                .observation(requestCreditDTO.getObservation())
                .status(requestCreditDTO.getStatus())
                .vehicle(buildVehicleDTOToVehicle(requestCreditDTO.getVehicleDTO()))
                .client(buildClientDTOToClient(requestCreditDTO.getClientDTO()))
                .yardCar(buildYardCarTDOToYardCar(requestCreditDTO.getYardCarDTO()))
                .executive(buildVehicleBrandDTOToExecutive(requestCreditDTO.getExecutiveDTO())).build();
    }

    private Vehicle buildVehicleDTOToVehicle(VehicleDTO vehicleDTO) {
        return Vehicle.builder().idVehicle(vehicleDTO.getIdVehicle()).licensePlate(vehicleDTO.getLicensePlate())
                .model(vehicleDTO.getModel())
                .chassisNumber(vehicleDTO.getChassisNumber())
                .type(vehicleDTO.getType())
                .cylinderCapacity(vehicleDTO.getCylinderCapacity())
                .appraise(vehicleDTO.getAppraise())
                .vehicleBrand(VehicleBrand.builder()
                        .id(vehicleDTO.getVehicleBrandDTO().getId())
                        .name(vehicleDTO.getVehicleBrandDTO().getName())
                        .build())
                .yardCar(YardCar.builder()
                        .idCar(vehicleDTO.getYardCarDTO().getIdCar())
                        .name(vehicleDTO.getYardCarDTO().getName())
                        .address(vehicleDTO.getYardCarDTO().getAddress())
                        .phone(vehicleDTO.getYardCarDTO().getPhone())
                        .numberPdv(vehicleDTO.getYardCarDTO().getNumberPdv())
                        .build())
                .anio(vehicleDTO.getAnio()).build();
    }

    private VehicleDTO buildVehicleToVehicleDTO(Vehicle vehicle) {
        return VehicleDTO.builder().idVehicle(vehicle.getIdVehicle())
                .licensePlate(vehicle.getLicensePlate())
                .model(vehicle.getModel())
                .chassisNumber(vehicle.getChassisNumber())
                .type(vehicle.getType())
                .cylinderCapacity(vehicle.getCylinderCapacity())
                .appraise(vehicle.getAppraise())
                .vehicleBrandDTO(VehicleBrandDTO.builder()
                        .id(vehicle.getVehicleBrand().getId())
                        .name(vehicle.getVehicleBrand().getName())
                        .build())
                .yardCarDTO(YardCarDTO.builder()
                        .idCar(vehicle.getYardCar().getIdCar())
                        .name(vehicle.getYardCar().getName())
                        .address(vehicle.getYardCar().getAddress())
                        .phone(vehicle.getYardCar().getPhone())
                        .numberPdv(vehicle.getYardCar().getNumberPdv())
                        .build())
                .anio(vehicle.getAnio()).build();
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

    private YardCar buildYardCarTDOToYardCar(YardCarDTO yardCarDTO) {
        return YardCar.builder()
                .idCar(yardCarDTO.getIdCar())
                .name(yardCarDTO.getName())
                .address(yardCarDTO.getAddress())
                .phone(yardCarDTO.getPhone())
                .numberPdv(yardCarDTO.getNumberPdv()).build();
    }

    private YardCarDTO buildYardCarToYardCarTDO(YardCar yardCar) {
        return YardCarDTO.builder()
                .idCar(yardCar.getIdCar())
                .name(yardCar.getName())
                .address(yardCar.getAddress())
                .phone(yardCar.getPhone())
                .numberPdv(yardCar.getNumberPdv()).build();
    }

    private Executive buildVehicleBrandDTOToExecutive(ExecutiveDTO executiveDTO) {
        return Executive.builder()
                .id(executiveDTO.getId())
                .identification(executiveDTO.getIdentification())
                .firstName(executiveDTO.getFirstName())
                .lastName(executiveDTO.getLastName())
                .address(executiveDTO.getAddress())
                .conventionalTelephone(executiveDTO.getConventionalTelephone())
                .mobile(executiveDTO.getMobile())
                .numberYardCars(executiveDTO.getNumberYardCars())
                .age(executiveDTO.getAge()).build();
    }


    private ExecutiveDTO buildExecutiveToExecutiveDTO(Executive executive) {
        return ExecutiveDTO.builder()
                .id(executive.getId())
                .identification(executive.getIdentification())
                .firstName(executive.getFirstName())
                .lastName(executive.getLastName())
                .address(executive.getAddress())
                .conventionalTelephone(executive.getConventionalTelephone())
                .mobile(executive.getMobile())
                .numberYardCars(executive.getNumberYardCars())
                .age(executive.getAge()).build();
    }


    private AsignationClientPatio buildAsignationClientPatio(Client client, YardCar yardCar) {
        return AsignationClientPatio.builder()
                .createdAsignation(new Date())
                .client(client)
                .yardCar(yardCar).build();
    }

    private RequestCreditDTO buildRequestCreditToRequestCreditTDO(RequestCredit requestCredit) {
        return RequestCreditDTO.builder()
                .idRequestCredit(requestCredit.getIdRequestCredit())
                .dateElaboration(requestCredit.getDateElaboration())
                .monthsTerm(requestCredit.getMonthsTerm())
                .entry(requestCredit.getEntry())
                .quotas(requestCredit.getQuotas())
                .observation(requestCredit.getObservation())
                .status(requestCredit.getStatus())
                .vehicleDTO(buildVehicleToVehicleDTO(requestCredit.getVehicle()))
                .clientDTO(buildClientToClientDTO(requestCredit.getClient()))
                .yardCarDTO(buildYardCarToYardCarTDO(requestCredit.getYardCar()))
                .executiveDTO(buildExecutiveToExecutiveDTO(requestCredit.getExecutive())).build();
    }
}
