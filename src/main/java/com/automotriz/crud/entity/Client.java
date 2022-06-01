package com.automotriz.crud.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue
    private UUID id;
    @NotNull(message = "Identificación no puede estar vacío")
    @Column(name = "identification")
    private String identification;
    private String firstName;
    private String lastName;
    private Integer age;
    @Temporal(TemporalType.DATE)
    private Date birthDate;
    private String address;
    private String phone;
    private String civilStatus;
    private String identificationSpouse;
    private String spouseName;
    @Column(name = "sujeto_credit_type")
    private String sujectCreditType;
    @OneToMany(mappedBy = "client")
    private List<RequestCredit> requestCreditList = new ArrayList<>();
    @OneToMany(mappedBy = "client")
    private List<AsignationClientPatio> asignationClientPatioList = new ArrayList<>();
}
