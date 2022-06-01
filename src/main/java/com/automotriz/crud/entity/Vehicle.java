package com.automotriz.crud.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "vehicle")
@Builder
@ToString(of = "licensePlate")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Vehicle {
    @Id
    @GeneratedValue
    private UUID idVehicle;
    @NotNull
    @Column(unique = true)
    private String licensePlate;
    @NotNull
    private String model;
    @NotNull
    private String chassisNumber;

    private String type;
    @NotNull
    private String cylinderCapacity;
    @NotNull
    private String appraise;
    @NotNull
    @ManyToOne
    private VehicleBrand vehicleBrand;
    @NotNull
    @ManyToOne
    private YardCar yardCar;
    @NotNull
    private Integer anio;
    @OneToMany(mappedBy = "vehicle")
    @Builder.Default
    private List<RequestCredit> requestCreditList = new ArrayList<>();
}
