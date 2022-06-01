package com.automotriz.crud.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class VehicleBrandDTO implements Serializable {
    private UUID id;
    @NotBlank(message = "El nombre de la marca es obligatorio")
    private String name;
}
