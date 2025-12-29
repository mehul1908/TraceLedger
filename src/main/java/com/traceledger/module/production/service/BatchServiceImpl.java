package com.traceledger.module.production.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.traceledger.module.production.dto.BatchRegisterModel;
import com.traceledger.module.production.entity.Batch;
import com.traceledger.module.production.entity.Factory;
import com.traceledger.module.production.entity.Product;
import com.traceledger.module.production.repo.BatchNoSequenceRepo;
import com.traceledger.module.production.repo.BatchRepo;
import com.traceledger.util.HashUtil;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class BatchServiceImpl implements BatchService {

	@Autowired
	private BatchNoSequenceRepo batchNoRepo;
	
	@Autowired
	private BatchRepo batchRepo;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private FactoryService factoryService;
	@Override
	@Transactional
	public void createBatch(@Valid BatchRegisterModel model) {

	    Product product = productService.findByProductCode(
	            model.getProductCode()
	    );

	    Factory factory = factoryService.findByFactoryCode(
	            model.getFactoryCode()
	    );

	    LocalDate manufactureDate = LocalDate.now();

	    int nextSeq = batchNoRepo.findMaxSeq(
	            manufactureDate,
	            product.getId(),
	            factory.getId()
	    ) + 1;

	    batchNoRepo.insertSeq(
	            manufactureDate,
	            product.getId(),
	            factory.getId(),
	            nextSeq
	    );

	    String datePart = manufactureDate.format(
	            DateTimeFormatter.ofPattern("yyMMdd")
	    );

	    String batchNo = String.format(
	            "B-%s%s%s%02d",
	            datePart,
	            product.getProductCode(),
	            factory.getFactoryCode(),
	            nextSeq
	    );

	    String canonical = String.join("|",
	            batchNo,
	            product.getProductHash(),
	            factory.getFactoryCode(),
	            manufactureDate.toString()
	    );

	    String batchHash = HashUtil.sha256(canonical);

	    Batch batch = Batch.builder()
	            .batchNo(batchNo)
	            .product(product)
	            .factory(factory)
	            .manufactureDate(manufactureDate)
	            .batchHash(batchHash)
	            .quantity(model.getQuantity())
	            .build();

	    batchRepo.save(batch);
	}


}
