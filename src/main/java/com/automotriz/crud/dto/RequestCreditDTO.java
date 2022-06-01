package com.automotriz.crud.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class RequestCreditDTO implements Serializable {
    private UUID idRequestCredit;
    private Date dateElaboration;

    private Integer monthsTerm;

    private Integer quotas;
    @Builder.Default
    private BigDecimal entry = BigDecimal.ZERO;
    private String observation;
    @NotBlank(message = "El estado es obligatorio")
    private String status;
    private ClientDTO clientDTO;
    private VehicleDTO vehicleDTO;
    private YardCarDTO yardCarDTO;
    private ExecutiveDTO executiveDTO;
}
