package com.automotriz.crud.service.impl;


import com.automotriz.crud.dto.ClientDTO;
import com.automotriz.crud.entity.Client;
import com.automotriz.crud.entity.Executive;
import com.automotriz.crud.entity.YardCar;
import com.automotriz.crud.enums.SujectCreditType;
import com.automotriz.crud.exception.ValidationServiceCustomer;
import com.automotriz.crud.repository.ClientRepository;
import com.automotriz.crud.service.ClientService;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<ClientDTO> getClients() {
        List<Client> clients = clientRepository.findAll();
        List<ClientDTO> clientDTOList = new ArrayList<>();
        clients.stream().forEach(client -> {
            clientDTOList.add(buildClientToClientDTO(client));
        });
        return clientDTOList;
    }

    @Override
    public Optional<ClientDTO> findByIdentification(String identification) {
        Optional<Client> client = clientRepository.findByIdentification(identification);

        if (client.isPresent()) {
            return Optional.of(buildClientToClientDTO(client.get())).stream().findFirst();
        } else {
            return Optional.empty();
        }

    }

    @Override
    public List<ClientDTO> getClientsByTypeClient(String sujectCreditType) {
        if (sujectCreditType.isBlank())
            throw new ValidationServiceCustomer("El campo de filtro es obligatorio", HttpStatus.PRECONDITION_FAILED);

        List<Client> clients = clientRepository.findBySujectCreditType(sujectCreditType);
        List<ClientDTO> clientDTOList = new ArrayList<>();
        clients.stream().forEach(client -> {
            clientDTOList.add(buildClientToClientDTO(client));
        });
        return clientDTOList;
    }

    @Override
    @Transactional
    public ClientDTO save(ClientDTO clientDTO) throws ValidationServiceCustomer {
        Optional<Client> clientDb = clientRepository.findByIdentification(clientDTO.getIdentification());
        if (clientDb.isPresent()) {
            throw new ValidationServiceCustomer("El cliente " + clientDTO.getIdentification() + " ya se encuentra registrado", HttpStatus.PRECONDITION_FAILED);
        } else {
            Client client = buildClientDTOToClient(clientDTO);
            client = clientRepository.save(client);
            return buildClientToClientDTO(client);
        }
    }

    @Override
    @Transactional
    public ClientDTO update(ClientDTO clientDTO) throws ValidationServiceCustomer {
        if (clientDTO.getId() == null)
            throw new ValidationServiceCustomer("Debe enviar el identificador del cliente a actualizar", HttpStatus.PRECONDITION_FAILED);

        Optional<Client> clientDb = clientRepository.findById(clientDTO.getId());
        if (clientDb.isPresent()) {
            Client client = buildClientDTOToClient(clientDTO);
            client = clientRepository.save(client);
            return buildClientToClientDTO(client);
        } else {
            throw new ValidationServiceCustomer("El cliente " + clientDTO.getIdentification() + " No se encuentra registrado", HttpStatus.PRECONDITION_FAILED);
        }
    }

    @Override
    @Transactional
    public void loadClientCSV() {
        try {
            if(!clientRepository.findAll().isEmpty())
                return ;
            //String path = "C:\\Users\\manab\\Desktop\\clients.csv";
            Resource resource = new ClassPathResource("/static/clients.csv");
            resource.getURL().getPath();
            //Se crea el objeto filereader
            FileReader filereader = new FileReader(resource.getURL().getPath());

            // crea el objeto  csvParser con el separador personalizado
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();

            /* se crea el objeto csvReader pasandole el parser y indicandole que empiece desde la segunda linea
            ya que la primera es el encabezado*/
            CSVReader csvReader = new CSVReaderBuilder(filereader).withCSVParser(parser).withSkipLines(1).build();
            String[] line;
            List<Client> listClient = new ArrayList<>();

            // se leen los datos linea a linea
            while ((line = csvReader.readNext()) != null) {
                listClient.add(buildClientCSV(line));
            }
            listClient.stream().forEach(client1 -> {
                //si no existe lo insertamos
                if (!clientRepository.findByIdentification(client1.getIdentification()).isPresent()) {
                    clientRepository.save(client1);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public boolean delete(String identification) throws ValidationServiceCustomer {
        if (identification.isBlank()) {
            throw new ValidationServiceCustomer("El número de identificación es obligatorio", HttpStatus.PRECONDITION_FAILED);
        }

        Optional<Client> client = clientRepository.findByIdentification(identification);

        if (client.isPresent()) {
            if (!client.get().getRequestCreditList().isEmpty() || !client.get().getAsignationClientPatioList().isEmpty()) {
                throw new ValidationServiceCustomer("El cliente " + client.get().getIdentification() + " No puede ser eliminado por sus dependencias ", HttpStatus.PRECONDITION_FAILED);
            }
            clientRepository.delete(client.get());
            return true;
        } else
            throw new ValidationServiceCustomer("El cliente " + client.get().getIdentification() + " no se encuentra registrado", HttpStatus.PRECONDITION_FAILED);

    }

    private Client buildClientCSV(String[] column) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Client client = new Client();
        client.setIdentification(column[0]);
        client.setFirstName(column[1] != null ? column[1] : "");
        client.setLastName(column[2] != null ? column[2] : "");
        client.setAge(column[3] != null ? Integer.valueOf(column[3]) : null);
        Date dataFormateada = format.parse(column[4] != null ? column[4] : null);
        client.setBirthDate(column[4] != null ? dataFormateada : null);
        client.setAddress(column[5] != null ? column[5] : "");
        client.setPhone(column[6] != null ? column[6] : "");
        client.setCivilStatus(column[7] != null ? column[7] : "");
        client.setIdentificationSpouse(column[8] != null ? column[8] : "");
        client.setSpouseName(column[9] != null ? column[9] : "");
        if (column[10] != null && (column[10].equals("SI")))
            client.setSujectCreditType(SujectCreditType.SujectCredit.getName());
        else
            client.setSujectCreditType(SujectCreditType.NotSujectCredit.getName());
        return client;
    }

    private Client buildClientDTOToClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setId(clientDTO.getId());
        client.setIdentification(clientDTO.getIdentification());
        client.setFirstName(clientDTO.getFirstName() != null ? clientDTO.getFirstName() : "");
        client.setLastName(clientDTO.getLastName() != null ? clientDTO.getLastName() : "");
        client.setAge(clientDTO.getAge() != null ? clientDTO.getAge() : null);
        client.setBirthDate(clientDTO.getBirthDate() != null ? clientDTO.getBirthDate() : null);
        client.setAddress(clientDTO.getAddress() != null ? clientDTO.getAddress() : "");
        client.setPhone(clientDTO.getPhone() != null ? clientDTO.getPhone() : "");
        client.setCivilStatus(clientDTO.getCivilStatus() != null ? clientDTO.getCivilStatus() : "");
        client.setIdentificationSpouse(clientDTO.getIdentificationSpouse() != null ? clientDTO.getIdentificationSpouse() : "");
        client.setSpouseName(clientDTO.getSpouseName() != null ? clientDTO.getSpouseName() : "");
        if (clientDTO.getSujectCreditType() != null && (clientDTO.getSujectCreditType().equals("Sujeto de crédito")))
            client.setSujectCreditType(SujectCreditType.SujectCredit.getName());
        else
            client.setSujectCreditType(SujectCreditType.NotSujectCredit.getName());
        return client;
    }

    private ClientDTO buildClientToClientDTO(Client client) {
        //ClientDTO clientDTO = new ClientDTO();
        return ClientDTO.builder()
                .id(client.getId())
                .identification(client.getIdentification())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .age(client.getAge())
                .birthDate(client.getBirthDate())
                .address(client.getAddress())
                .phone(client.getPhone())
                .civilStatus(client.getCivilStatus())
                .identificationSpouse(client.getIdentificationSpouse())
                .spouseName(client.getSpouseName())
                .sujectCreditType((client.getSujectCreditType().equals("Sujeto de crédito")) ? SujectCreditType.SujectCredit.getName() : SujectCreditType.NotSujectCredit.getName()).build();
        /*
        clientDTO.setIdentification(client.getIdentification());
        clientDTO.setId(client.getId());
        clientDTO.setFirstName(client.getFirstName() != null ? client.getFirstName() : "");
        clientDTO.setLastName(client.getLastName() != null ? client.getLastName() : "");
        clientDTO.setAge(client.getAge() != null ? client.getAge() : null);
        clientDTO.setBirthDate(client.getBirthDate() != null ? client.getBirthDate() : null);
        clientDTO.setAddress(client.getAddress() != null ? client.getAddress() : "");
        clientDTO.setPhone(client.getPhone() != null ? client.getPhone() : "");
        clientDTO.setCivilStatus(client.getCivilStatus() != null ? client.getCivilStatus() : "");
        clientDTO.setIdentificationSpouse(client.getIdentificationSpouse() != null ? client.getIdentificationSpouse() : "");
        clientDTO.setSpouseName(client.getSpouseName() != null ? client.getSpouseName() : "");
        if (client.getSujectCreditType() != null && (client.getSujectCreditType().equals("Sujeto de crédito")))
            clientDTO.setSujectCreditType(SujectCreditType.SujectCredit.getName());
        else
            clientDTO.setSujectCreditType(SujectCreditType.NotSujectCredit.getName());
        return clientDTO;*/
    }
}
