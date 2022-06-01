package com.automotriz.crud.controller;

import com.automotriz.crud.dto.ExecutiveDTO;
import com.automotriz.crud.exception.ValidationServiceCustomer;
import com.automotriz.crud.service.ExecutiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/executives")
public class ExecutiveController {
    @Autowired
    private ExecutiveService executiveService;

    @PostConstruct
    public void loader() {
        executiveService.loadExecutiveCSV();
    }

    @PostMapping("/save")
    public ResponseEntity<ExecutiveDTO> save(@RequestBody @Valid ExecutiveDTO executiveDTO) throws ValidationServiceCustomer {
        Map<String, Object> errorResponse = new HashMap<>();
        try {
            ExecutiveDTO executiveDTO1 = executiveService.save(executiveDTO);
            return new ResponseEntity(executiveDTO1, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            errorResponse.put("Error:", e.getMostSpecificCause().getMessage());
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/all")
    public ResponseEntity<List<ExecutiveDTO>> getExecutives() {
        List<ExecutiveDTO> executiveDTOList = executiveService.getExecutives();
        if (executiveDTOList.isEmpty()) {
            return new ResponseEntity("Sin información", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<List<ExecutiveDTO>>(executiveDTOList, HttpStatus.OK);
        }

    }

    @GetMapping("/findByNumberYardCars")
    public ResponseEntity<?> getExecutiveFindByNumberYardCars(@RequestParam(name = "numberYardCars", required = true) String numberYardCars) {
        List<ExecutiveDTO> executiveDTOList = executiveService.getExecutivesFindByNumberYardCars(numberYardCars);

        if (executiveDTOList.isEmpty()) {
            return new ResponseEntity("No existe información", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<List<ExecutiveDTO>>(executiveDTOList, HttpStatus.OK);
        }

    }
}
