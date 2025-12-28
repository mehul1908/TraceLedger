package com.traceledger.module.product.service;

import com.traceledger.module.product.dto.ProductRegisterModel;

import jakarta.validation.Valid;

public interface ProductService {

	void createProduct(@Valid ProductRegisterModel model);

}
