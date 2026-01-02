package com.traceledger.module.shipment.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traceledger.module.shipment.entity.Shipment;

@Repository
public interface ShipmentRepo extends JpaRepository<Shipment, Long> {

}
