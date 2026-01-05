package com.traceledger.module.production.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.traceledger.util.HashUtil;

@Service
public class HashService {

    public String generateProductHash(String productCode, String name, BigDecimal mrp) {
        String canonical = String.join("|", productCode, name.trim(), mrp.setScale(2, RoundingMode.HALF_UP).toString());
        return sha256(canonical);
    }

    public String generateBatchHash(String batchNo, String productHash, String factoryCode, LocalDate manufactureDate) {
        String canonical = String.join("|", batchNo, productHash, factoryCode, manufactureDate.toString());
        return sha256(canonical);
    }

    private String sha256(String input) {
        return HashUtil.sha256(input);
    }
}

