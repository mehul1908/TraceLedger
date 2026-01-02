package com.traceledger.module.shipment.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traceledger.module.shipment.entity.ShipmentItem;

@Repository
public interface ShipmentItemRepo extends JpaRepository<ShipmentItem, Long> {

}
