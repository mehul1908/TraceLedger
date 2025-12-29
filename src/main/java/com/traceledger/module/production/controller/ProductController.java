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
import com.traceledger.module.production.dto.ProductRegisterModel;
import com.traceledger.module.production.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	/*------------------Product---------------------------------*/
	@PostMapping("/")
	@PreAuthorize("hasRole('MANUFACTURER')")
	public ResponseEntity<ApiResponse> createProduct(@RequestBody @Valid ProductRegisterModel model){
		productService.createProduct(model);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, null, "Product is created"));

	}
	
}
