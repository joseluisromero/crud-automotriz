package com.automotriz.crud.controller;

import com.automotriz.crud.dto.YardCarDTO;
import com.automotriz.crud.exception.ValidationServiceCustomer;
import com.automotriz.crud.service.YardCarService;
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
@RequestMapping("/yardCar")
public class YardCarController {
    @Autowired
    private YardCarService yardCarService;

    @PostMapping("/save")
    public ResponseEntity<YardCarDTO> save(@RequestBody @Valid YardCarDTO yardCarDTO) throws ValidationServiceCustomer {
        try {
            YardCarDTO yardCarDTO1 = yardCarService.save(yardCarDTO);
            return new ResponseEntity(yardCarDTO1, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<YardCarDTO> update(@RequestBody @Valid YardCarDTO yardCarDTO) throws ValidationServiceCustomer {
        try {
            YardCarDTO yardCarDTO1 = yardCarService.update(yardCarDTO);
            return new ResponseEntity(yardCarDTO1, HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/all")
    public ResponseEntity<List<YardCarDTO>> getYardCars() {
        try {
            List<YardCarDTO> yardCarDTOList = yardCarService.getYardCars();
            return new ResponseEntity<List<YardCarDTO>>(yardCarDTOList, HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/findByNumberPdv")
    public ResponseEntity<YardCarDTO> getFindByNumberPdv(@RequestParam(value = "numberPdv", required = true) @NotNull String numberPdv) {
        try {
            Optional<YardCarDTO> yardCarDTO = yardCarService.findByNumberPdv(numberPdv);
            if (yardCarDTO.isEmpty()) {
                return new ResponseEntity("Info: No se encontraron registros", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<YardCarDTO>(yardCarDTO.get(), HttpStatus.OK);
            }
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{numberPdv}")
    public ResponseEntity<YardCarDTO> deleteYardCarFindById(@PathVariable(value = "numberPdv", required = true) @NotBlank String numberPdv) throws ValidationServiceCustomer {
        boolean delete = false;
        try {
            delete = yardCarService.delete(numberPdv);
            if (delete) {
                return new ResponseEntity("Patio de Auto numero: " + numberPdv + " eliminado exitosamente", HttpStatus.OK);
            } else {
                return new ResponseEntity("Mensaje: El  Patio de auto numero: " + numberPdv + " no pudo ser eliminado", HttpStatus.BAD_REQUEST);
            }
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.NOT_FOUND);
        }

    }
}
