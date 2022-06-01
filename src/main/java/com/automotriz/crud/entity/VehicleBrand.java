package com.automotriz.crud.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@Entity
@Table(name = "vehicle_brand")
@ToString(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleBrand {
    @Id
    @GeneratedValue
    private UUID id;
    //@NotBlank(message = "La marca no puede estar vacia")
    private String name;
}
