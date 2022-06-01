package com.automotriz.crud.controller;

import com.automotriz.crud.dto.AsignationClientPatioDTO;
import com.automotriz.crud.exception.ValidationServiceCustomer;
import com.automotriz.crud.service.AsignationClientPatioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/asignation")
public class AsignationClientPatioController {
    @Autowired
    private AsignationClientPatioService asignationClientPatioService;

    @PostMapping("/save")
    public ResponseEntity<AsignationClientPatioDTO> save(@RequestBody @Valid AsignationClientPatioDTO asignationClientPatioDTO) throws ValidationServiceCustomer {
        try {
            AsignationClientPatioDTO asignationClientPatioDTO1 = asignationClientPatioService.save(asignationClientPatioDTO);
            return new ResponseEntity(asignationClientPatioDTO1, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/update")
    public ResponseEntity<AsignationClientPatioDTO> update(@RequestBody @Valid AsignationClientPatioDTO asignationClientPatioDTO) throws ValidationServiceCustomer {
        try {
            AsignationClientPatioDTO asignationClientPatioDTO1 = asignationClientPatioService.update(asignationClientPatioDTO);
            return new ResponseEntity(asignationClientPatioDTO1, HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/all")
    public ResponseEntity<List<AsignationClientPatioDTO>> getYardCars() {
        try {
            List<AsignationClientPatioDTO> asignationClientPatioDTOList = asignationClientPatioService.getAllAsignationClient();
            return new ResponseEntity<List<AsignationClientPatioDTO>>(asignationClientPatioDTOList, HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @DeleteMapping("/delete/{idAsignation}")
    public ResponseEntity<AsignationClientPatioDTO> deleteYardCarFindById(@PathVariable(value = "idAsignation", required = true) @NotBlank String idAsignation) throws ValidationServiceCustomer {
        boolean delete = false;

        try {
            UUID id = UUID.fromString(idAsignation);
            delete = asignationClientPatioService.delete(id);
            if (delete) {
                return new ResponseEntity("La asignación: " + id + " eliminado exitosamente", HttpStatus.OK);
            } else {
                return new ResponseEntity("Mensaje: La asignación con id: " + id + " no pudo ser eliminado", HttpStatus.BAD_REQUEST);
            }
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.NOT_FOUND);
        }

    }
}
