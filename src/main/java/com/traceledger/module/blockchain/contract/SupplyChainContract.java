package com.traceledger.module.blockchain.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;

import com.traceledger.module.blockchain.dto.BatchTransferredEventResponse;

import org.web3j.protocol.Web3j;
import org.web3j.crypto.Credentials;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.protocol.core.methods.request.EthFilter;
import io.reactivex.Flowable;

public class SupplyChainContract extends Contract {

    protected SupplyChainContract(
            String contractAddress,
            Web3j web3j,
            Credentials credentials,
            ContractGasProvider gasProvider
    ) {
        super("", contractAddress, web3j, credentials, gasProvider);
    }

    public static SupplyChainContract load(
            String contractAddress,
            Web3j web3j,
            Credentials credentials,
            ContractGasProvider gasProvider
    ) {
        return new SupplyChainContract(
                contractAddress, web3j, credentials, gasProvider
        );
    }

    public RemoteFunctionCall<TransactionReceipt> transferBatch(
            byte[] batchHash,
            String to,
            BigInteger quantity
    ) {
        Function function = new Function(
                "transferBatch",
                Arrays.asList(
                        new Bytes32(batchHash),
                        new Address(to),
                        new Uint256(quantity)
                ),
                Collections.emptyList()
        );

        return executeRemoteCallTransaction(function);
    }
    
    public static final Event BATCHTRANSFERRED_EVENT = new Event(
            "BatchTransferred",
            Arrays.asList(
                    new TypeReference<Bytes32>(true) {},   // batchHash (indexed)
                    new TypeReference<Address>(true) {},   // from (indexed)
                    new TypeReference<Address>(true) {},   // to (indexed)
                    new TypeReference<Uint256>() {},        // quantity
                    new TypeReference<Uint256>() {}         // timestamp
            )
    );
    
    

    public Flowable<BatchTransferredEventResponse> batchTransferredEventFlowable(
            DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock
    ) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BATCHTRANSFERRED_EVENT));

        return web3j.ethLogFlowable(filter).map(log -> {
            BatchTransferredEventResponse response =
                    new BatchTransferredEventResponse();
            response.log = log;

            EventValues eventValues =
                    staticExtractEventParameters(BATCHTRANSFERRED_EVENT, log);

            response.batchHash = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            response.from = eventValues.getIndexedValues().get(1).getValue().toString();
            response.to = eventValues.getIndexedValues().get(2).getValue().toString();
            response.quantity =
                    (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            response.timestamp =
                    (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();

            return response;
        });
    }

}
