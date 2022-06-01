package com.automotriz.crud.controller;

import com.automotriz.crud.dto.ClientDTO;
import com.automotriz.crud.dto.YardCarDTO;
import com.automotriz.crud.exception.ValidationServiceCustomer;
import com.automotriz.crud.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @PostConstruct
    public void loader() {
        clientService.loadClientCSV();
    }

    @PostMapping("/save")
    public ResponseEntity<ClientDTO> save(@RequestBody @Valid ClientDTO clientDTO) throws ValidationServiceCustomer {
        try {
            ClientDTO clientDTO1 = clientService.save(clientDTO);
            return new ResponseEntity(clientDTO1, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ClientDTO> update(@RequestBody @Valid ClientDTO clientDTO) throws ValidationServiceCustomer {
        try {
            ClientDTO clientDTO1 = clientService.update(clientDTO);
            return new ResponseEntity(clientDTO1, HttpStatus.OK);
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getClients() {
        List<ClientDTO> clientDTOList = clientService.getClients();
        if (clientDTOList.isEmpty()) {
            return new ResponseEntity<>("Sin información", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<List<ClientDTO>>(clientDTOList, HttpStatus.OK);
        }

    }

    @GetMapping("/findBySujectCreditType")
    public ResponseEntity<List<ClientDTO>> getClientsFindBySujectCreditType(@RequestParam(name = "sujectCreditType", required = true) String sujectCreditType) {
        List<ClientDTO> clientDTOList = clientService.getClientsByTypeClient(sujectCreditType);
        if (clientDTOList.isEmpty()) {
            return new ResponseEntity("Mensaje:" + "No se encontraron registros con el filtro: ".concat(sujectCreditType), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<List<ClientDTO>>(clientDTOList, HttpStatus.OK);
        }
    }

    @GetMapping("/findByIdentification")
    public ResponseEntity<ClientDTO> getClientsfindByIdentification(@RequestParam(name = "identification", required = true) String identification) {
        Optional<ClientDTO> clientDTO = clientService.findByIdentification(identification);
        if (clientDTO.isPresent()) {
            return new ResponseEntity<ClientDTO>(clientDTO.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity("Mensaje:" + "No se encontraron registros con el filtro: ".concat(identification), HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{identification}")
    public ResponseEntity<ClientDTO> deleteClientFindByIdentification(@PathVariable(value = "identification", required = true) @NotBlank String identification) throws ValidationServiceCustomer {
        boolean delete = false;
        try {
            delete = clientService.delete(identification);
            if (delete) {
                return new ResponseEntity("El cliente con identificación: " + identification + " fue eliminado exitosamente", HttpStatus.OK);
            } else {
                return new ResponseEntity("Mensaje: El cliente con identificación: " + identification + " no pudo ser eliminado", HttpStatus.BAD_REQUEST);
            }
        } catch (DataAccessException e) {
            return new ResponseEntity("Error: " + e.getMostSpecificCause().getMessage(), HttpStatus.NOT_FOUND);
        }

    }
}
