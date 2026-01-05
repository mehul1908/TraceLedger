package com.traceledger.module.blockchain.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockchainTransferData{
	    private String txHash;
	    private String batchHash;
	    private Integer quantity;
	    private BigInteger blockNumber;
}

