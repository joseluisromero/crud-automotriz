package com.automotriz.crud.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "request_credit")
@Builder
@ToString(of = "idRequestCredit")
@NoArgsConstructor
@AllArgsConstructor
public class RequestCredit {
    @Id
    @GeneratedValue
    private UUID idRequestCredit;
    @Temporal(TemporalType.DATE)
    private Date dateElaboration;

    private Integer monthsTerm;

    private Integer quotas;

    private BigDecimal entry;
    private String status;

    private String observation;
    @ManyToOne
    private Client client;
    @ManyToOne
    private Vehicle vehicle;
    @ManyToOne
    private YardCar yardCar;
    @ManyToOne
    private Executive executive;
}
