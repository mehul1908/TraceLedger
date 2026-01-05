package com.traceledger.module.shipment.listener;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.traceledger.module.blockchain.entity.BlockchainTxIntent;
import com.traceledger.module.blockchain.enums.IntentStatus;
import com.traceledger.module.blockchain.repo.IntentRepo;
import com.traceledger.module.blockchain.service.BlockchainService;
import com.traceledger.module.shipment.entity.Shipment;
import com.traceledger.module.shipment.entity.ShipmentItem;
import com.traceledger.module.shipment.enums.ShipmentStatus;
import com.traceledger.module.shipment.record.ShipmentDispatchedEvent;
import com.traceledger.module.shipment.repo.ShipmentRepo;

@Component
public class ShipmentEventListener {

    @Autowired private ShipmentRepo shipmentRepo;
    @Autowired private BlockchainService blockchainService;
    @Autowired private IntentRepo intentRepo;

    @Async
    @EventListener
    public void handleShipmentDispatch(ShipmentDispatchedEvent event) {
        Shipment shipment = shipmentRepo.findById(event.shipmentId()).orElseThrow();

        for (ShipmentItem item : shipment.getItems()) {
            BlockchainTxIntent intent = BlockchainTxIntent.builder()
                    .batchHash(item.getBatch().getBatchHash())
                    .quantity(item.getQuantity())
                    .shipment(shipment)
                    .fromUser(shipment.getFromUser())
                    .toUser(shipment.getToUser())
                    .status(IntentStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .build();

            intentRepo.save(intent);

            String txHash = blockchainService.transferBatch(
                    item.getBatch().getBatchHash(),
                    shipment.getToUser().getWalletAddress(),
                    item.getQuantity()
            ).toLowerCase();

            intent.setTxHash(txHash);
            intentRepo.save(intent);
        }

        shipment.setStatus(ShipmentStatus.DISPATCHED);
        shipmentRepo.save(shipment);
    }
}

