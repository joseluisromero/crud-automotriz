package com.automotriz.crud.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class ExecutiveDTO implements Serializable {

    private UUID id;
    @NotBlank(message = "La identificación es obligatorio")
    private String identification;
    private String firstName;
    private String lastName;
    private Integer age;
    private String address;
    private String conventionalTelephone;
    private String mobile;
    private String numberYardCars;
}
