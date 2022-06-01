package com.automotriz.crud.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "asignation_client_patio")
@Builder
@ToString(of = "idAsignation")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AsignationClientPatio {
    @Id
    @GeneratedValue
    private UUID idAsignation;
    @ManyToOne
    private Client client;
    @ManyToOne
    private YardCar yardCar;
    @Temporal(TemporalType.DATE)
    private Date createdAsignation;
}
