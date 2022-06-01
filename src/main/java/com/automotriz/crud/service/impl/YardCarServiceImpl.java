package com.automotriz.crud.service.impl;

import com.automotriz.crud.dto.YardCarDTO;
import com.automotriz.crud.entity.Executive;
import com.automotriz.crud.entity.YardCar;
import com.automotriz.crud.exception.ValidationServiceCustomer;
import com.automotriz.crud.repository.ExecutiveRepository;
import com.automotriz.crud.repository.YardCarRepository;
import com.automotriz.crud.service.YardCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class YardCarServiceImpl implements YardCarService {

    @Autowired
    private YardCarRepository yardCarRepository;
    @Autowired
    ExecutiveRepository executiveRepository;

    @Transactional
    @Override
    public YardCarDTO save(YardCarDTO yardCarDTO) throws ValidationServiceCustomer {

        Optional<YardCar> yardCarDb = yardCarRepository.findByNumberPdv(yardCarDTO.getNumberPdv());
        if (yardCarDb.isPresent()) {
            throw new ValidationServiceCustomer("El Patio de auto ya se encuentra registrado ", HttpStatus.PRECONDITION_FAILED);
        } else {
            YardCar yardCar = buildYardCarTDOToYardCar(yardCarDTO);
            yardCar = yardCarRepository.save(yardCar);
            return buildYardCarToYardCarTDO(yardCar);
        }
    }

    @Transactional
    @Override
    public YardCarDTO update(YardCarDTO yardCarDTO) throws ValidationServiceCustomer {

        Optional<YardCar> yardCarDb = yardCarRepository.findByNumberPdv(yardCarDTO.getNumberPdv());
        if (yardCarDb.isPresent()) {
            YardCar yardCar = buildYardCarTDOToYardCar(yardCarDTO);
            yardCar = yardCarRepository.save(yardCar);
            return buildYardCarToYardCarTDO(yardCar);
        } else {
            throw new ValidationServiceCustomer("El Patio de auto número: " + yardCarDTO.getNumberPdv() + " no se encuentra registrado ", HttpStatus.PRECONDITION_FAILED);
        }
    }

    @Override
    public Optional<YardCarDTO> findByNumberPdv(String numberPdv) {
        Optional<YardCar> yardCar = yardCarRepository.findByNumberPdv(numberPdv);
        if (yardCar.isPresent()) {
            return Optional.of(buildYardCarToYardCarTDO(yardCar.get()));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public boolean delete(String numberPdv) throws ValidationServiceCustomer {
        if (numberPdv.isBlank()) {
            throw new ValidationServiceCustomer("El número del Patio de Auto es obligatorio", HttpStatus.PRECONDITION_FAILED);
        }
        Optional<YardCar> yardCar = yardCarRepository.findByNumberPdv(numberPdv);
        if (yardCar.isPresent()) {
            Set<Executive> executives = new HashSet<>();
            executives = executiveRepository.findByNumberYardCars(numberPdv);
            if (!executives.isEmpty() || !yardCar.get().getVehicleList().isEmpty() || !yardCar.get().getAsignationClientPatioList().isEmpty() || !yardCar.get().getRequestCreditList().isEmpty()) {
                throw new ValidationServiceCustomer("El Patio de auto número " + numberPdv + " no puede ser eliminado por sus dependencias", HttpStatus.PRECONDITION_FAILED);
            }
            yardCarRepository.delete(yardCar.get());
            return true;
        } else {
            throw new ValidationServiceCustomer("El Patio de auto numero " + numberPdv + " no se encuentra registrado", HttpStatus.PRECONDITION_FAILED);
        }

    }

    @Override
    public List<YardCarDTO> getYardCars() {
        List<YardCar> executiveList = yardCarRepository.findAll();
        List<YardCarDTO> yardCarDTOList = new ArrayList<>();
        executiveList.stream().forEach(yardCar -> {
            yardCarDTOList.add(buildYardCarToYardCarTDO(yardCar));
        });
        return yardCarDTOList;
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
}
