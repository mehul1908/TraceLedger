package com.traceledger.module.production.service;

import java.util.Optional;

import com.traceledger.module.production.dto.BatchRegisterModel;
import com.traceledger.module.production.entity.Batch;

import jakarta.validation.Valid;

public interface BatchService {

	void createBatch(@Valid BatchRegisterModel model);

	Batch getBatchById(Long batchId);

	Optional<Batch> getBatchOptionalByBatchHash(String batchHash);

}
