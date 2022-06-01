package com.automotriz.crud.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class YardCarDTO implements Serializable {
    private UUID idCar;
    @NotBlank(message = "el nombre es obligatorio")
    private String name;
    @NotBlank(message = "La dirección es obligatorio")
    private String address;
    @NotBlank(message = "el telefono es obligatorio")
    private String phone;
    @NotBlank(message = "el número del patio de auto es obligatorio")
    private String numberPdv;
}
