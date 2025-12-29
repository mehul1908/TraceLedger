package com.traceledger.module.production.service;

import com.traceledger.module.production.dto.BatchRegisterModel;

import jakarta.validation.Valid;

public interface BatchService {

	void createBatch(@Valid BatchRegisterModel model);

}
