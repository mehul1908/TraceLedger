package com.traceledger.module.blockchain.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traceledger.dto.ApiResponse;
import com.traceledger.module.blockchain.dto.BlockchainTamperData;
import com.traceledger.module.blockchain.service.BlockchainService;

@RestController
@RequestMapping("/blockchain")
public class BlockchainController {

	@Autowired
	private BlockchainService blockchainService;
	
	@PostMapping("/replay")
	public ResponseEntity<ApiResponse> replay() throws IOException, Exception{
		List<BlockchainTamperData> list = blockchainService.replay();
		return ResponseEntity.ok(new ApiResponse(true , list , null));
	}
	
}
