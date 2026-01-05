package com.traceledger.module.shipment.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.traceledger.module.production.entity.Batch;
import com.traceledger.module.shipment.entity.Shipment;
import com.traceledger.module.shipment.entity.ShipmentItem;

@Repository
public interface ShipmentItemRepo extends JpaRepository<ShipmentItem, Long> {

	@Query("""
	        select si.shipment.shipmentHash
	        from ShipmentItem si
	        where si.batch.batchHash = :batchHash
	    """)
	    Optional<String> findShipmentHashByBatchHash(String batchHash);

	Optional<ShipmentItem> findByShipmentAndBatch(Shipment shipment, Batch batch);

}
