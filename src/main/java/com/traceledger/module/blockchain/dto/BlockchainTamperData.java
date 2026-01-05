package com.traceledger.module.blockchain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockchainTamperData {

	private String txHash;
	
	private Long shipmentId;
	
	private String batchHash;
	
	private String error;
	
}

