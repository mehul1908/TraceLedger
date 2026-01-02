package com.traceledger.module.blockchain.service;

public interface BlockchainService {

    String transferBatch(
        String batchHash,
        String toWallet,
        int quantity
    );
}

