package com.traceledger.module.blockchain.service;

import java.io.IOException;
import java.util.List;

import com.traceledger.module.blockchain.dto.BlockchainTamperData;

public interface BlockchainService {

    String transferBatch(
        String batchHash,
        String toWallet,
        int quantity
    );

	List<BlockchainTamperData> replay() throws IOException, Exception;
}

