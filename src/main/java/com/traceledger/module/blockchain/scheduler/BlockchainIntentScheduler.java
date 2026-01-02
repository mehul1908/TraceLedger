package com.traceledger.module.blockchain.scheduler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import com.traceledger.module.blockchain.entity.BlockchainTransaction;
import com.traceledger.module.blockchain.entity.BlockchainTxIntent;
import com.traceledger.module.blockchain.enums.IntentStatus;
import com.traceledger.module.blockchain.listener.BlockchainEventListener;
import com.traceledger.module.blockchain.repo.BlockchainTransactionRepo;
import com.traceledger.module.blockchain.repo.IntentRepo;

@Component
public class BlockchainIntentScheduler {

    private final Web3j web3j;

	@Autowired
    private IntentRepo intentRepo;


    BlockchainIntentScheduler(Web3j web3j) {
        this.web3j = web3j;
    }

    @Autowired
    private BlockchainTransactionRepo txRepo;
    
    @Autowired
    private BlockchainEventListener eventListener;
    

    @Scheduled(fixedDelay = 10 * 60 * 1000) // every 10 minutes
    public void markStuckIntents() throws IOException {

        List<BlockchainTxIntent> stuckIntents =
                intentRepo.findByStatusAndCreatedAtBefore(
                        IntentStatus.PENDING,
                        LocalDateTime.now().minusMinutes(15)
                );

        for (BlockchainTxIntent intent : stuckIntents) {
        	if (web3j.ethGetTransactionReceipt(intent.getTxHash())
        	        .send()
        	        .getTransactionReceipt()
        	        .isPresent()) {
        	    continue; // mined, don't mark failed
        	}

            intent.setStatus(IntentStatus.FAILED);
            intentRepo.save(intent);
        }
    }
    
    
    @Scheduled(fixedDelay = 60_000)
    public void reconcileMinedButUnconfirmedIntents() throws IOException {

        List<BlockchainTxIntent> pending =
                intentRepo.findByStatus(IntentStatus.PENDING);

        for (BlockchainTxIntent intent : pending) {

            Optional<TransactionReceipt> receipt =
                    web3j.ethGetTransactionReceipt(intent.getTxHash())
                         .send()
                         .getTransactionReceipt();

            if (receipt.isEmpty()) continue;

            // Tx mined but event listener missed it
            // Manually finalize
            BlockchainTransaction tx =
                    BlockchainTransaction.builder()
                            .txHash(intent.getTxHash())
                            .batchHash(intent.getBatchHash())
                            .quantity(intent.getQuantity())
                            .shipment(intent.getShipment())
                            .fromUser(intent.getFromUser())
                            .toUser(intent.getToUser())
                            .createdAt(LocalDateTime.now())
                            .build();

            txRepo.save(tx);

            intent.setStatus(IntentStatus.CONFIRMED);
            intentRepo.save(intent);

            eventListener.finalizeShipmentIfComplete(intent.getShipment());
        }
    }

}

