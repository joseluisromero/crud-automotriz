package com.automotriz.crud.repository;

import com.automotriz.crud.entity.YardCar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface YardCarRepository extends JpaRepository<YardCar, UUID> {

    Optional<YardCar> findByNumberPdv(String numberPdv);
}
