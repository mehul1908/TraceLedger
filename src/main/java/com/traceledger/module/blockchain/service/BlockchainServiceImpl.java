package com.traceledger.module.blockchain.service;

import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Numeric;

import com.traceledger.module.blockchain.contract.SupplyChainContract;

@Service
public class BlockchainServiceImpl implements BlockchainService {

    @Autowired
    private SupplyChainContract contract;

    @Override
    public String transferBatch(
            String batchHash,
            String toWallet,
            int quantity
    ) {
        try {
            TransactionReceipt receipt =
                contract.transferBatch(
                        Numeric.hexStringToByteArray(batchHash),
                        toWallet,
                        BigInteger.valueOf(quantity)
                ).send();

            return receipt.getTransactionHash();

        } catch (Exception e) {
            throw new IllegalStateException("Blockchain transfer failed", e);
        }
    }
}

