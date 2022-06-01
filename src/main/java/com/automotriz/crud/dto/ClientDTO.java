package com.automotriz.crud.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class ClientDTO implements Serializable {
    private UUID id;
    @NotBlank(message = "La identificación es obligatorio")
    private String identification;
    @NotBlank(message = "Nombres es obligatorio")
    private String firstName;
    @NotBlank(message = "Apellidos es obligatorio")
    private String lastName;
    @PositiveOrZero
    private Integer age;
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthDate;
    @NotBlank(message = "Dirección es obligatorio")
    private String address;
    @NotBlank(message = "Teléfono es obligatorio")
    private String phone;
    @NotBlank(message = "Estado civil es obligatorio")
    private String civilStatus;
    private String identificationSpouse;
    private String spouseName;
    @NotBlank(message = "Si es sujeto a crédito es obligatorio")
    private String sujectCreditType;

}
