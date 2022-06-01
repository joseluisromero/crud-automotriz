package com.automotriz.crud.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Entity
@Table(name = "executive")
@Builder
@ToString(of = "identification")
@NoArgsConstructor
@AllArgsConstructor
public class Executive {
    @Id
    @GeneratedValue
    private UUID id;
    @NotNull(message = "Identificación no puede estar vacío")
    @Column(name = "identification")
    private String identification;
    private String firstName;
    private String lastName;
    private Integer age;
    private String address;
    private String conventionalTelephone;
    private String mobile;
    private String numberYardCars;
}
