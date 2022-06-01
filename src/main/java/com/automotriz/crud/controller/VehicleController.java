package com.automotriz.crud.controller;

import com.automotriz.crud.dto.VehicleDTO;
import com.automotriz.crud.exception.ValidationServiceCustomer;
import com.automotriz.crud.service.VehicleService;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/save")
    public ResponseEntity<VehicleDTO> save(@RequestBody @Valid VehicleDTO vehicleDTO) throws ValidationServiceCustomer {
        try {
            VehicleDTO vehicleDTO1 = vehicleService.save(vehicleDTO);
            return new ResponseEntity(vehicleDTO1, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<VehicleDTO> update(@RequestBody @Valid VehicleDTO vehicleDTO) throws ValidationServiceCustomer {
        try {
            VehicleDTO vehicleDTO1 = vehicleService.update(vehicleDTO);
            return new ResponseEntity(vehicleDTO1, HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/all")
    public ResponseEntity<List<VehicleDTO>> getAllsVehicles() {
        try {
            List<VehicleDTO> vehicleDTOList = vehicleService.getAllsVehicles();
            return new ResponseEntity<List<VehicleDTO>>(vehicleDTOList, HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/findByLicensePlate")
    public ResponseEntity<VehicleDTO> getFindByLicensePlate(@RequestParam(value = "licensePlate", required = true) @NotNull String licensePlate) {
        try {
            Optional<VehicleDTO> vehicleDTO = vehicleService.findByLicensePlate(licensePlate);
            if (vehicleDTO.isEmpty()) {
                return new ResponseEntity("Info: No se encontraron registros", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<VehicleDTO>(vehicleDTO.get(), HttpStatus.OK);
            }
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{licensePlate}")
    public ResponseEntity<VehicleDTO> deleteYardCarFindById(@PathVariable(value = "licensePlate", required = true) @NotBlank String licensePlate) throws ValidationServiceCustomer {
        boolean delete = false;
        try {
            delete = vehicleService.delete(licensePlate);
            if (delete) {
                return new ResponseEntity("Vehículo con placa numero: " + licensePlate + " eliminado exitosamente", HttpStatus.OK);
            } else {
                return new ResponseEntity("Mensaje: El Vehículo con placa numero: " + licensePlate + " no pudo ser eliminado", HttpStatus.BAD_REQUEST);
            }
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/findBy")
    public ResponseEntity<List<VehicleDTO>> getFindByLicensePlateOrModelOrAnio(@RequestParam(value = "licensePlate", required = false) String licensePlate,
                                                                               @RequestParam(value = "model", required = false) String model,
                                                                               @RequestParam(value = "anio", required = false) String anio) {
        try {
            List<VehicleDTO> vehicleDTOList = vehicleService.findByLicensePlateOrModelOrAnio(licensePlate, model, anio.isBlank() ? null : Integer.parseInt(anio));
            if (vehicleDTOList.isEmpty()) {
                return new ResponseEntity("Info: No se encontraron registros", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<List<VehicleDTO>>(vehicleDTOList, HttpStatus.OK);
            }
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
