package com.traceledger.module.product.service;

import com.traceledger.module.product.dto.FactoryRegisterModel;

import jakarta.validation.Valid;

public interface FactoryService {

	void createFactory(@Valid FactoryRegisterModel model);

}
