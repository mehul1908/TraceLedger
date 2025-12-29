package com.traceledger.module.production.service;

import com.traceledger.module.production.dto.ProductRegisterModel;
import com.traceledger.module.production.entity.Product;

import jakarta.validation.Valid;

public interface ProductService {

	void createProduct(@Valid ProductRegisterModel model);

	Product findByProductCode(String productCode);

}
