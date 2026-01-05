package com.traceledger.module.production.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.traceledger.module.production.repo.BatchNoSequenceRepo;
import com.traceledger.module.production.repo.FactorySequenceRepo;
import com.traceledger.module.production.repo.ProductSequenceRepo;

import jakarta.transaction.Transactional;

@Service
public class SequenceGeneratorService {

    @Autowired
    private BatchNoSequenceRepo batchNoRepo;

    @Autowired
    private ProductSequenceRepo prodSeqRepo;

    @Autowired
    private FactorySequenceRepo factSeqRepo;

    @Transactional
    public synchronized int nextBatchSeq(Long productId, Long factoryId, LocalDate manufactureDate) {
        int nextSeq = batchNoRepo.findMaxSeq(manufactureDate, productId, factoryId) + 1;
        batchNoRepo.insertSeq(manufactureDate, productId, factoryId, nextSeq);
        return nextSeq;
    }

    @Transactional
    public synchronized long nextProductSeq() {
        prodSeqRepo.insertDummyRow();
        return prodSeqRepo.getLastInsertedId();
    }

    @Transactional
    public synchronized long nextFactorySeq() {
        factSeqRepo.insertDummyRow();
        return factSeqRepo.getLastInsertedId();
    }
}

