package com.traceledger.module.production.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traceledger.dto.ApiResponse;
import com.traceledger.module.production.dto.BatchRegisterModel;
import com.traceledger.module.production.service.BatchService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/batch")
public class BatchController {

	@Autowired
	private BatchService batchService;
	
	@PostMapping("/")
	@Transactional
	public ResponseEntity<ApiResponse> createBatch(@RequestBody @Valid BatchRegisterModel model ){
		batchService.createBatch(model);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, null, "Batch is created"));
	}
}
