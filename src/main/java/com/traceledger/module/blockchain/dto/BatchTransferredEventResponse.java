package com.traceledger.module.blockchain.dto;

import java.math.BigInteger;

import org.web3j.protocol.core.methods.response.Log;

public class BatchTransferredEventResponse {
    public Log log;

    public byte[] batchHash;
    public String from;
    public String to;
    public BigInteger quantity;
    public BigInteger timestamp;
}

