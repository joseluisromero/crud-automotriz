package com.automotriz.crud.dto;


import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class AsignationClientPatioDTO implements Serializable {

    private UUID idAsignation;

    private ClientDTO clientDTO;

    private YardCarDTO yardCarDTO;

    private Date createdAsignation;
}
