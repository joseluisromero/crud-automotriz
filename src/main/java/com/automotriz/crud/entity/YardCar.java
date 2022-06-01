package com.automotriz.crud.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "yard_card")
@Builder
@ToString(of = "numberPdv")
@NoArgsConstructor
@AllArgsConstructor
public class YardCar {
    @Id
    @GeneratedValue
    private UUID idCar;
    @NotNull
    private String name;
    @NotNull
    private String address;
    @NotNull
    private String phone;
    @NotNull
    @Column(name = "number_pdv")
    private String numberPdv;

    @OneToMany(mappedBy = "yardCar", fetch = FetchType.EAGER)
    @Builder.Default
    List<Vehicle> vehicleList = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "yardCar")
    List<AsignationClientPatio> asignationClientPatioList = new ArrayList<>();
    @Builder.Default
    @OneToMany(mappedBy = "yardCar")
    List<RequestCredit> requestCreditList = new ArrayList<>();
}
