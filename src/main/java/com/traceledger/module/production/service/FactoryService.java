package com.traceledger.module.production.service;

import com.traceledger.module.production.dto.FactoryRegisterModel;
import com.traceledger.module.production.entity.Factory;

import jakarta.validation.Valid;

public interface FactoryService {

	void createFactory(@Valid FactoryRegisterModel model);

	Factory findByFactoryCode(String factoryCode);

}
