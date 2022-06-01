package com.automotriz.crud.controller;

import com.automotriz.crud.dto.RequestCreditDTO;
import com.automotriz.crud.exception.ValidationServiceCustomer;
import com.automotriz.crud.service.RequestCreditService;
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
@RequestMapping("/request")
public class RequestCreditController {
    @Autowired
    private RequestCreditService requestCreditService;


    @PostMapping("/save")
    public ResponseEntity<RequestCreditDTO> save(@RequestBody @Valid RequestCreditDTO requestCreditDTO) throws ValidationServiceCustomer {
        try {
            RequestCreditDTO requestCreditDTO1 = requestCreditService.save(requestCreditDTO);
            return new ResponseEntity<RequestCreditDTO>(requestCreditDTO1, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<RequestCreditDTO> update(@RequestBody @Valid RequestCreditDTO requestCreditDTO) throws ValidationServiceCustomer {
        try {
            RequestCreditDTO requestCreditDTO1 = requestCreditService.update(requestCreditDTO);
            return new ResponseEntity<RequestCreditDTO>(requestCreditDTO1, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getRequestCredit() {
        List<RequestCreditDTO> requestCreditDTOList = requestCreditService.getRequestCreditAll();
        if (requestCreditDTOList.isEmpty()) {
            return new ResponseEntity("Sin información", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<List<RequestCreditDTO>>(requestCreditDTOList, HttpStatus.OK);
        }
    }

    @GetMapping("/findByStatus")
    public ResponseEntity<List<?>> getRequestByStatus(@RequestParam(name = "status") String status) throws ValidationServiceCustomer {
        List<RequestCreditDTO> requestCreditDTOList = requestCreditService.getRequestByStatus(status);
        if (requestCreditDTOList.isEmpty()) {

            return new ResponseEntity("Mensaje:" + " No se encontraron registros con el filtro: ".concat(status), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<List<?>>(requestCreditDTOList, HttpStatus.OK);
        }

    }
}
