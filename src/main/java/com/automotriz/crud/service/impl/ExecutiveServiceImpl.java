package com.automotriz.crud.service.impl;

import com.automotriz.crud.dto.ExecutiveDTO;
import com.automotriz.crud.entity.Executive;
import com.automotriz.crud.exception.ValidationServiceCustomer;
import com.automotriz.crud.repository.ExecutiveRepository;
import com.automotriz.crud.service.ExecutiveService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ExecutiveServiceImpl implements ExecutiveService {
    @Autowired
    private ExecutiveRepository executiveRepository;
    Logger logger = LoggerFactory.getLogger(ExecutiveServiceImpl.class);

    @Transactional
    @Override
    public ExecutiveDTO save(ExecutiveDTO executiveDTO) throws ValidationServiceCustomer {
        Optional<Executive> clientDb = executiveRepository.findByIdentification(executiveDTO.getIdentification());
        if (clientDb.isPresent()) {
            throw new ValidationServiceCustomer("El Ejecutivo ya existe " + executiveDTO.getIdentification(), HttpStatus.PRECONDITION_FAILED);
        }
        Executive executive = buildVehicleBrandDTOToExecutive(executiveDTO);
        executive = executiveRepository.save(executive);
        return buildExecutiveToExecutiveDTO(executive);
    }

    @Transactional
    @Override
    public void loadExecutiveCSV() {
        try {
            if(!executiveRepository.findAll().isEmpty())
                return;
            Resource resource = new ClassPathResource("/static/executives.csv");
            resource.getURL().getPath();
            //Se crea el objeto filereader
            FileReader filereader = new FileReader(resource.getURL().getPath());

            // crea el objeto  csvParser con el separador personalizado
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();

            /* se crea el objeto csvReader pasandole el parser y indicandole que empiece desde la segunda linea
            ya que la primera es el encabezado*/
            CSVReader csvReader = new CSVReaderBuilder(filereader).withCSVParser(parser).withSkipLines(1).build();
            String[] line;
            List<Executive> listExecutive = new ArrayList<>();

            // se leen los datos linea a linea
            while ((line = csvReader.readNext()) != null) {
                listExecutive.add(buildExecutiveCSV(line));
            }
            List<String> listaSinDuplicados = listExecutive.stream()
                    .map(item -> item.getIdentification())
                    .distinct()
                    .collect(Collectors.toList());
            if (listaSinDuplicados.size() == listExecutive.size()) {
                listExecutive.stream().forEach(executive -> {
                    //si no existe lo insertamos
                    if (!executiveRepository.findByIdentification(executive.getIdentification()).isPresent()) {
                        executiveRepository.save(executive);
                    }
                });
            } else {
                //System.err.println("Error al cargar ejecutivos registros duplicados");
                logger.error("Error carga Csv ", "Archivo con registros duplicados");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ExecutiveDTO> getExecutives() {
        List<Executive> executiveList = executiveRepository.findAll();
        List<ExecutiveDTO> executiveDTOList = new ArrayList<>();
        executiveList.stream().forEach(executive -> {
            executiveDTOList.add(buildExecutiveToExecutiveDTO(executive));
        });
        return executiveDTOList;
    }

    @Override
    public List<ExecutiveDTO> getExecutivesFindByNumberYardCars(String numberYardCars) {
        Set<Executive> executiveList = executiveRepository.findByNumberYardCars(numberYardCars);
        List<ExecutiveDTO> executiveDTOList = new ArrayList<>();
        executiveList.stream().forEach(executive -> {
            executiveDTOList.add(buildExecutiveToExecutiveDTO(executive));
        });
        return executiveDTOList;
    }

    @Override
    public Optional<ExecutiveDTO> findByIdentification(String identification) {
        Optional<Executive> executive = executiveRepository.findByIdentification(identification);
        if (executive.isPresent()) {
            return Optional.of(buildExecutiveToExecutiveDTO(executive.get()));
        }
        return Optional.empty();
    }

    private Executive buildExecutiveCSV(String[] column) throws ParseException {
        Executive executive = new Executive();
        executive.setIdentification(column[0]);
        executive.setFirstName(column[1] != null ? column[1] : "");
        executive.setLastName(column[2] != null ? column[2] : "");
        executive.setAddress(column[3] != null ? column[3] : null);
        executive.setConventionalTelephone(column[4] != null ? column[4] : null);
        executive.setMobile(column[5] != null ? column[5] : null);
        executive.setNumberYardCars(column[6] != null ? column[6] : null);
        executive.setAge(column[7] != null ? Integer.valueOf(column[7]) : null);
        return executive;
    }

    private Executive buildVehicleBrandDTOToExecutive(ExecutiveDTO executiveDTO) {
        return Executive.builder()
                .identification(executiveDTO.getIdentification())
                .firstName(executiveDTO.getFirstName())
                .lastName(executiveDTO.getLastName())
                .address(executiveDTO.getAddress())
                .conventionalTelephone(executiveDTO.getConventionalTelephone())
                .mobile(executiveDTO.getMobile())
                .numberYardCars(executiveDTO.getNumberYardCars())
                .age(executiveDTO.getAge()).build();
    }

    private ExecutiveDTO buildExecutiveToExecutiveDTO(Executive executive) {
        return ExecutiveDTO.builder()
                .id(executive.getId())
                .identification(executive.getIdentification())
                .firstName(executive.getFirstName())
                .lastName(executive.getLastName())
                .address(executive.getAddress())
                .conventionalTelephone(executive.getConventionalTelephone())
                .mobile(executive.getMobile())
                .numberYardCars(executive.getNumberYardCars())
                .age(executive.getAge()).build();
    }
}
