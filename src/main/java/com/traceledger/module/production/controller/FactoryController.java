package com.traceledger.module.production.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traceledger.dto.ApiResponse;
import com.traceledger.module.production.dto.FactoryRegisterModel;
import com.traceledger.module.production.service.FactoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/factory")
public class FactoryController {

	@Autowired
	private FactoryService factoryService;
	
	@PostMapping("/")
	@PreAuthorize("hasRole('MANUFACTURER')")
	public ResponseEntity<ApiResponse> createFactory(@RequestBody @Valid FactoryRegisterModel model){
		factoryService.createFactory(model);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, null, "Factory is created"));
	}
}
