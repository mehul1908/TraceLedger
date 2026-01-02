package com.traceledger.module.blockchain.listener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.utils.Numeric;

import com.traceledger.module.blockchain.contract.SupplyChainContract;
import com.traceledger.module.blockchain.dto.BatchTransferredEventResponse;
import com.traceledger.module.blockchain.entity.BlockchainTransaction;
import com.traceledger.module.blockchain.entity.BlockchainTxIntent;
import com.traceledger.module.blockchain.enums.IntentStatus;
import com.traceledger.module.blockchain.repo.BlockchainTransactionRepo;
import com.traceledger.module.blockchain.repo.IntentRepo;
import com.traceledger.module.shipment.entity.Shipment;
import com.traceledger.module.shipment.enums.ShipmentStatus;
import com.traceledger.module.shipment.repo.ShipmentRepo;

@Slf4j
@Component
public class BlockchainEventListener {

    private final SupplyChainContract contract;
    private final BlockchainTransactionRepo txRepo;
    
    @Autowired
    private IntentRepo intentRepo;
    
    @Autowired
    private ShipmentRepo shipRepo;

    public BlockchainEventListener(
            SupplyChainContract contract,
            BlockchainTransactionRepo txRepo
    ) {
        this.contract = contract;
        this.txRepo = txRepo;
    }

    @PostConstruct
    public void startListening() {

        contract.batchTransferredEventFlowable(
                DefaultBlockParameterName.LATEST,
                DefaultBlockParameterName.LATEST
        ).subscribe(
            event -> handleEventAsync(event),
            Throwable::printStackTrace
        );
    }

    @Async("blockchainExecutor")
    @Transactional
    public void handleEventAsync(BatchTransferredEventResponse event) {

        String txHash = event.log.getTransactionHash().toLowerCase();
        log.info("📦 Processing event async for tx {}", txHash);

        BlockchainTxIntent intent =
                intentRepo.findByTxHash(txHash).orElse(null);

        if (intent == null) {
            log.warn("⏳ Intent not yet found for tx {}", txHash);
            return;
        }

        if (intent.getStatus() == IntentStatus.CONFIRMED) {
            log.warn("⚠️ Intent already confirmed for tx {}", txHash);
            return;
        }

        BlockchainTransaction tx =
                BlockchainTransaction.builder()
                        .txHash(txHash)
                        .batchHash(Numeric.toHexString(event.batchHash))
                        .quantity(event.quantity.intValue())
                        .shipment(intent.getShipment())
                        .fromUser(intent.getFromUser())
                        .toUser(intent.getToUser())
                        .createdAt(
                            LocalDateTime.ofInstant(
                                Instant.ofEpochSecond(event.timestamp.longValue()),
                                ZoneId.systemDefault()
                            )
                        )
                        .build();

        txRepo.save(tx);

        intent.setStatus(IntentStatus.CONFIRMED);
        intentRepo.save(intent);

        finalizeShipmentIfComplete(intent.getShipment());

        log.info("✅ BlockchainTransaction saved for tx {}", txHash);
    }
    
    public void finalizeShipmentIfComplete(Shipment shipment) {
        boolean allConfirmed =
                intentRepo.existsByShipmentAndStatus(
                        shipment, IntentStatus.PENDING
                ) == false;

        if (allConfirmed) {
            shipment.setStatus(ShipmentStatus.DISPATCHED);
            shipRepo.save(shipment);
        }
    }
}
