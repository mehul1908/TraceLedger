package com.traceledger.module.blockchain.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.traceledger.module.blockchain.entity.BlockchainTxIntent;
import com.traceledger.module.blockchain.enums.IntentStatus;
import com.traceledger.module.shipment.entity.Shipment;

public interface IntentRepo extends JpaRepository<BlockchainTxIntent, Long>{

	Optional<BlockchainTxIntent> findByTxHash(String txHash);

	boolean existsByShipmentAndStatus(Shipment shipment, IntentStatus pending);

	List<BlockchainTxIntent> findByStatusAndCreatedAtBefore(
		    IntentStatus status,
		    LocalDateTime time
		);

	List<BlockchainTxIntent> findByStatus(IntentStatus pending);

}
