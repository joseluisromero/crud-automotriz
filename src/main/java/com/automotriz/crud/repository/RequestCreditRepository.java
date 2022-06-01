package com.automotriz.crud.repository;

import com.automotriz.crud.entity.Client;
import com.automotriz.crud.entity.RequestCredit;
import com.automotriz.crud.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RequestCreditRepository extends JpaRepository<RequestCredit, UUID> {
    Optional<RequestCredit> findByClientAndVehicleAndDateElaboration(Client client, Vehicle vehicle, Date dateElaboration);

    Optional<RequestCredit> findByClientAndStatusAndDateElaboration(Client client, String status, Date dateElaboration);

    Optional<RequestCredit> findByVehicleAndStatus(Vehicle vehicle, String status);

    List<RequestCredit> findByStatus(String status);

}
