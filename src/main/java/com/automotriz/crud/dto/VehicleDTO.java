package com.automotriz.crud.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
public class VehicleDTO implements Serializable {

    private UUID idVehicle;
    @NotBlank(message = "La placa es obligatorio")
    private String licensePlate;
    @NotBlank(message = "El modelo es obligatorio")
    private String model;
    @NotBlank(message = "El número de chasis es obligatorio")
    private String chassisNumber;
    private String type;
    @NotBlank(message = "El Cilindraje es obligatorio")
    private String cylinderCapacity;
    @NotBlank(message = "El Avalúo es obligatorio")
    private String appraise;

    private VehicleBrandDTO vehicleBrandDTO;
    private Integer anio;
    private YardCarDTO yardCarDTO;
}
