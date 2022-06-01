package com.automotriz.crud.controller;

import com.automotriz.crud.dto.VehicleBrandDTO;
import com.automotriz.crud.exception.ValidationServiceCustomer;
import com.automotriz.crud.service.VehicleBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/vehicleBrand")
public class VehicleBrandController {
    @Autowired
    private VehicleBrandService vehicleBrandService;

    @PostConstruct
    public void loader() {
        vehicleBrandService.loadVehicleBrandCSV();
    }

    @PostMapping("/save")
    public ResponseEntity<VehicleBrandDTO> save(@RequestBody @Valid VehicleBrandDTO vehicleBrandDTO) throws ValidationServiceCustomer {
        Map<String, Object> errorResponse = new HashMap<>();
        try {
            VehicleBrandDTO vehicleBrandDTO1 = vehicleBrandService.save(vehicleBrandDTO);
            return new ResponseEntity<VehicleBrandDTO>(vehicleBrandDTO1, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            errorResponse.put("Error:", e.getMostSpecificCause().getMessage());
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<VehicleBrandDTO>> getVehicleBrandAll() {
        List<VehicleBrandDTO> vehicleBrandDTOList = vehicleBrandService.getVehicleBrand();
        if (vehicleBrandDTOList.isEmpty()) {
            return new ResponseEntity("Sin registros", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<List<VehicleBrandDTO>>(vehicleBrandDTOList, HttpStatus.OK);
        }

    }

    @GetMapping("/findByName")
    public ResponseEntity<VehicleBrandDTO> getVehicleBrandFindByName(@RequestParam(name = "name", required = true) String name) throws ValidationServiceCustomer {
        Optional<VehicleBrandDTO> vehicleBrand = vehicleBrandService.findByName(name);
        if (vehicleBrand.isPresent()) {
            return new ResponseEntity<VehicleBrandDTO>(vehicleBrand.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity("Mensaje:" + " No se encontraron registros con el filtro: ".concat(name), HttpStatus.NOT_FOUND);
        }
    }
}
