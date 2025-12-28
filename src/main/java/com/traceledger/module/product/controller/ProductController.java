package com.traceledger.module.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traceledger.dto.ApiResponse;
import com.traceledger.module.product.dto.FactoryRegisterModel;
import com.traceledger.module.product.dto.ProductRegisterModel;
import com.traceledger.module.product.service.FactoryService;
import com.traceledger.module.product.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private FactoryService  factoryService;
	
	@Autowired
	private ProductService productService;
	
	/*-----------------------Factory--------------------------*/
	
	@PostMapping("/factory")
	@PreAuthorize("hasRole('MANUFACTURER')")
	public ResponseEntity<ApiResponse> createFactory(@RequestBody @Valid FactoryRegisterModel model){
		factoryService.createFactory(model);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, null, "Factory is created"));
	}
	
	
	/*------------------Product---------------------------------*/
	@PostMapping("/")
	@PreAuthorize("hasRole('MANUFACTURER')")
	public ResponseEntity<ApiResponse> createProduct(@RequestBody @Valid ProductRegisterModel model){
		productService.createProduct(model);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, null, "Product is created"));

	}
	
}
