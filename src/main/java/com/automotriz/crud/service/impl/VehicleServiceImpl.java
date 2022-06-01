package com.automotriz.crud.service.impl;

import com.automotriz.crud.dto.VehicleBrandDTO;
import com.automotriz.crud.dto.VehicleDTO;
import com.automotriz.crud.dto.YardCarDTO;
import com.automotriz.crud.entity.Vehicle;
import com.automotriz.crud.entity.VehicleBrand;
import com.automotriz.crud.entity.YardCar;
import com.automotriz.crud.exception.ValidationServiceCustomer;
import com.automotriz.crud.repository.VehicleRepository;
import com.automotriz.crud.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImpl implements VehicleService {
    @Autowired
    VehicleRepository vehicleRepository;

    @Override
    @Transactional
    public VehicleDTO save(VehicleDTO vehicleDTO) throws ValidationServiceCustomer {
        Optional<Vehicle> vehicleDb = vehicleRepository.findByLicensePlate(vehicleDTO.getLicensePlate());
        if (vehicleDb.isPresent())
            throw new ValidationServiceCustomer("El vehiculo con placa :" + vehicleDTO.getLicensePlate() + " ya se encuentra registrado", HttpStatus.PRECONDITION_FAILED);
        else {
            Vehicle vehicle = buildVehicleDTOToVehicle(vehicleDTO);
            vehicle = vehicleRepository.save(vehicle);
            return buildVehicleToVehicleDTO(vehicle);
        }
    }

    @Override
    @Transactional
    public VehicleDTO update(VehicleDTO vehicleDTO) throws ValidationServiceCustomer {
        if (vehicleDTO.getIdVehicle() == null)
            throw new ValidationServiceCustomer("El identificador unico del registros es obligatorio", HttpStatus.PRECONDITION_FAILED);

        Optional<Vehicle> vehicleDb = vehicleRepository.findById(vehicleDTO.getIdVehicle());
        if (vehicleDb.isPresent()) {
            Vehicle vehicle = buildVehicleDTOToVehicle(vehicleDTO);
            vehicle = vehicleRepository.save(vehicle);
            return buildVehicleToVehicleDTO(vehicle);
        } else {
            throw new ValidationServiceCustomer("El vehiculo con placa :" + vehicleDTO.getLicensePlate() + " no se encuentra registrado", HttpStatus.PRECONDITION_FAILED);
        }
    }

    @Override
    public Optional<VehicleDTO> findByLicensePlate(String licensePlate) {
        if (licensePlate.isBlank())
            throw new ValidationServiceCustomer("La placa es obligatorio para el filtro", HttpStatus.PRECONDITION_FAILED);
        Optional<Vehicle> vehicleDb = vehicleRepository.findByLicensePlate(licensePlate);
        if (vehicleDb.isPresent())
            return Optional.of(buildVehicleToVehicleDTO(vehicleDb.get()));
        else
            return Optional.empty();
    }

    @Override
    public List<VehicleDTO> getAllsVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<VehicleDTO> vehicleDTOList = new ArrayList<>();
        vehicles.stream().forEach(vehicle -> {
            vehicleDTOList.add(buildVehicleToVehicleDTO(vehicle));
        });
        return vehicleDTOList;
    }

    @Override
    public List<VehicleDTO> findByLicensePlateOrModelOrAnio(String licensePlate, String model, Integer anio) {
        List<Vehicle> vehicles = vehicleRepository.findByLicensePlateOrModelOrAnio(licensePlate.isBlank() ? null : licensePlate, model.isBlank() ? null : model, anio == null ? null : anio);
        List<VehicleDTO> vehicleDTOList = new ArrayList<>();
        vehicles.stream().forEach(vehicle -> {
            vehicleDTOList.add(buildVehicleToVehicleDTO(vehicle));
        });
        return vehicleDTOList;
    }

    @Override
    @Transactional
    public boolean delete(String licensePlate) throws ValidationServiceCustomer {
        if (licensePlate.isBlank())
            throw new ValidationServiceCustomer("La placa es obligatorio para eliminar el vehiculo", HttpStatus.PRECONDITION_FAILED);
        Optional<Vehicle> vehicleDb = vehicleRepository.findByLicensePlate(licensePlate);
        if (vehicleDb.isPresent()) {
            if (!vehicleDb.get().getRequestCreditList().isEmpty())
                throw new ValidationServiceCustomer("El vehiculo con placa " + vehicleDb.get().getLicensePlate() + " no puede ser eliminado por sus dependencias", HttpStatus.PRECONDITION_FAILED);

            vehicleRepository.delete(vehicleDb.get());
            return true;
        } else
            throw new ValidationServiceCustomer("El vehiculo con placa: " + licensePlate + " no se encuentra registrado", HttpStatus.PRECONDITION_FAILED);
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
}
