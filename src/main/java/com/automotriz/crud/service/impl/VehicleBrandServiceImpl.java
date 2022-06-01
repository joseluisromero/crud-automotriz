package com.automotriz.crud.service.impl;

import com.automotriz.crud.dto.VehicleBrandDTO;
import com.automotriz.crud.entity.VehicleBrand;
import com.automotriz.crud.exception.ValidationServiceCustomer;
import com.automotriz.crud.repository.VehicleBrandRepository;
import com.automotriz.crud.service.VehicleBrandService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleBrandServiceImpl implements VehicleBrandService {
    @Autowired
    private VehicleBrandRepository vehicleBrandRepository;

    @Override
    @Transactional
    public void loadVehicleBrandCSV() {
        try {
            if (!vehicleBrandRepository.findAll().isEmpty())
                return;
            Resource resource = new ClassPathResource("/static/vehicle_brand.csv");
            resource.getURL().getPath();
            //Se crea el objeto filereader
            FileReader filereader = new FileReader(resource.getURL().getPath());

            // crea el objeto  csvParser con el separador personalizado
            // CSVParser parser = new CSVParserBuilder().withSeparator(';').build();

            /* se crea el objeto csvReader pasandole el parser y indicandole que empiece desde la segunda linea
            ya que la primera es el encabezado*/
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            String[] line;
            List<VehicleBrand> listVehicleBrand = new ArrayList<>();

            // se leen los datos linea a linea
            while ((line = csvReader.readNext()) != null) {
                listVehicleBrand.add(VehicleBrand.builder().name(line[0] != null ? line[0] : "").build());
            }
            listVehicleBrand.stream().forEach(vehicleBrand -> {
                //si no existe lo insertamos
                if (!vehicleBrandRepository.findByName(vehicleBrand.getName()).isPresent()) {
                    vehicleBrandRepository.save(vehicleBrand);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<VehicleBrandDTO> getVehicleBrand() {
        List<VehicleBrand> vehicleBrandList = vehicleBrandRepository.findAll();
        List<VehicleBrandDTO> vehicleBrandDTOList = new ArrayList<>();
        vehicleBrandList.stream().forEach(vehicleBrand -> {
            vehicleBrandDTOList.add(buildVehicleBrandToVehicleBrandDTO(vehicleBrand));
        });
        return vehicleBrandDTOList;
    }

    @Override
    public Optional<VehicleBrandDTO> findByName(String name) throws ValidationServiceCustomer {
        if (name.isBlank())
            throw new ValidationServiceCustomer("El campo de filtro es obligatorio", HttpStatus.PRECONDITION_FAILED);
        Optional<VehicleBrand> vehicleBrand = vehicleBrandRepository.findByName(name);
        if (vehicleBrand.isPresent()) {
            return Optional.of(buildVehicleBrandToVehicleBrandDTO(vehicleBrand.get()));
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public VehicleBrandDTO save(VehicleBrandDTO vehicleBrandDTO) throws ValidationServiceCustomer {
        Optional<VehicleBrand> vehicleBrandDb = vehicleBrandRepository.findByName(vehicleBrandDTO.getName());
        if (vehicleBrandDb.isPresent()) {
            throw new ValidationServiceCustomer("La marca " + vehicleBrandDTO.getName() + " ya se encuentra registrado", HttpStatus.PRECONDITION_FAILED);
        } else {
            VehicleBrand vehicleBrand = buildVehicleBrandDTOToVehicleBrand(vehicleBrandDTO);
            vehicleBrand = vehicleBrandRepository.save(vehicleBrand);
            return buildVehicleBrandToVehicleBrandDTO(vehicleBrand);
        }
    }


    private VehicleBrand buildVehicleBrandDTOToVehicleBrand(VehicleBrandDTO vehicleBrandDTO) {
        return VehicleBrand.builder().name(vehicleBrandDTO.getName()).build();
    }

    private VehicleBrandDTO buildVehicleBrandToVehicleBrandDTO(VehicleBrand vehicleBrand) {
        return VehicleBrandDTO.builder().id(vehicleBrand.getId()).name(vehicleBrand.getName()).build();
    }
}
