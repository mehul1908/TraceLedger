package com.traceledger.module.blockchain.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.Log;
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
	
	
	private static final String BINARY = ""; // you can keep empty if not deploying
    private static final String ABI = "[\r\n"
    		+ "	{\r\n"
    		+ "		\"anonymous\": false,\r\n"
    		+ "		\"inputs\": [\r\n"
    		+ "			{\r\n"
    		+ "				\"indexed\": true,\r\n"
    		+ "				\"internalType\": \"bytes32\",\r\n"
    		+ "				\"name\": \"batchHash\",\r\n"
    		+ "				\"type\": \"bytes32\"\r\n"
    		+ "			},\r\n"
    		+ "			{\r\n"
    		+ "				\"indexed\": true,\r\n"
    		+ "				\"internalType\": \"bytes32\",\r\n"
    		+ "				\"name\": \"productHash\",\r\n"
    		+ "				\"type\": \"bytes32\"\r\n"
    		+ "			},\r\n"
    		+ "			{\r\n"
    		+ "				\"indexed\": true,\r\n"
    		+ "				\"internalType\": \"address\",\r\n"
    		+ "				\"name\": \"owner\",\r\n"
    		+ "				\"type\": \"address\"\r\n"
    		+ "			},\r\n"
    		+ "			{\r\n"
    		+ "				\"indexed\": false,\r\n"
    		+ "				\"internalType\": \"uint256\",\r\n"
    		+ "				\"name\": \"timestamp\",\r\n"
    		+ "				\"type\": \"uint256\"\r\n"
    		+ "			}\r\n"
    		+ "		],\r\n"
    		+ "		\"name\": \"BatchRegistered\",\r\n"
    		+ "		\"type\": \"event\"\r\n"
    		+ "	},\r\n"
    		+ "	{\r\n"
    		+ "		\"anonymous\": false,\r\n"
    		+ "		\"inputs\": [\r\n"
    		+ "			{\r\n"
    		+ "				\"indexed\": true,\r\n"
    		+ "				\"internalType\": \"bytes32\",\r\n"
    		+ "				\"name\": \"batchHash\",\r\n"
    		+ "				\"type\": \"bytes32\"\r\n"
    		+ "			},\r\n"
    		+ "			{\r\n"
    		+ "				\"indexed\": true,\r\n"
    		+ "				\"internalType\": \"address\",\r\n"
    		+ "				\"name\": \"from\",\r\n"
    		+ "				\"type\": \"address\"\r\n"
    		+ "			},\r\n"
    		+ "			{\r\n"
    		+ "				\"indexed\": true,\r\n"
    		+ "				\"internalType\": \"address\",\r\n"
    		+ "				\"name\": \"to\",\r\n"
    		+ "				\"type\": \"address\"\r\n"
    		+ "			},\r\n"
    		+ "			{\r\n"
    		+ "				\"indexed\": false,\r\n"
    		+ "				\"internalType\": \"uint256\",\r\n"
    		+ "				\"name\": \"quantity\",\r\n"
    		+ "				\"type\": \"uint256\"\r\n"
    		+ "			},\r\n"
    		+ "			{\r\n"
    		+ "				\"indexed\": false,\r\n"
    		+ "				\"internalType\": \"uint256\",\r\n"
    		+ "				\"name\": \"timestamp\",\r\n"
    		+ "				\"type\": \"uint256\"\r\n"
    		+ "			}\r\n"
    		+ "		],\r\n"
    		+ "		\"name\": \"BatchTransferred\",\r\n"
    		+ "		\"type\": \"event\"\r\n"
    		+ "	},\r\n"
    		+ "	{\r\n"
    		+ "		\"anonymous\": false,\r\n"
    		+ "		\"inputs\": [\r\n"
    		+ "			{\r\n"
    		+ "				\"indexed\": true,\r\n"
    		+ "				\"internalType\": \"bytes32\",\r\n"
    		+ "				\"name\": \"productHash\",\r\n"
    		+ "				\"type\": \"bytes32\"\r\n"
    		+ "			},\r\n"
    		+ "			{\r\n"
    		+ "				\"indexed\": true,\r\n"
    		+ "				\"internalType\": \"address\",\r\n"
    		+ "				\"name\": \"manufacturer\",\r\n"
    		+ "				\"type\": \"address\"\r\n"
    		+ "			},\r\n"
    		+ "			{\r\n"
    		+ "				\"indexed\": false,\r\n"
    		+ "				\"internalType\": \"uint256\",\r\n"
    		+ "				\"name\": \"timestamp\",\r\n"
    		+ "				\"type\": \"uint256\"\r\n"
    		+ "			}\r\n"
    		+ "		],\r\n"
    		+ "		\"name\": \"ProductRegistered\",\r\n"
    		+ "		\"type\": \"event\"\r\n"
    		+ "	},\r\n"
    		+ "	{\r\n"
    		+ "		\"inputs\": [\r\n"
    		+ "			{\r\n"
    		+ "				\"internalType\": \"bytes32\",\r\n"
    		+ "				\"name\": \"batchHash\",\r\n"
    		+ "				\"type\": \"bytes32\"\r\n"
    		+ "			},\r\n"
    		+ "			{\r\n"
    		+ "				\"internalType\": \"bytes32\",\r\n"
    		+ "				\"name\": \"productHash\",\r\n"
    		+ "				\"type\": \"bytes32\"\r\n"
    		+ "			}\r\n"
    		+ "		],\r\n"
    		+ "		\"name\": \"registerBatch\",\r\n"
    		+ "		\"outputs\": [],\r\n"
    		+ "		\"stateMutability\": \"nonpayable\",\r\n"
    		+ "		\"type\": \"function\"\r\n"
    		+ "	},\r\n"
    		+ "	{\r\n"
    		+ "		\"inputs\": [\r\n"
    		+ "			{\r\n"
    		+ "				\"internalType\": \"bytes32\",\r\n"
    		+ "				\"name\": \"productHash\",\r\n"
    		+ "				\"type\": \"bytes32\"\r\n"
    		+ "			}\r\n"
    		+ "		],\r\n"
    		+ "		\"name\": \"registerProduct\",\r\n"
    		+ "		\"outputs\": [],\r\n"
    		+ "		\"stateMutability\": \"nonpayable\",\r\n"
    		+ "		\"type\": \"function\"\r\n"
    		+ "	},\r\n"
    		+ "	{\r\n"
    		+ "		\"inputs\": [\r\n"
    		+ "			{\r\n"
    		+ "				\"internalType\": \"bytes32\",\r\n"
    		+ "				\"name\": \"batchHash\",\r\n"
    		+ "				\"type\": \"bytes32\"\r\n"
    		+ "			},\r\n"
    		+ "			{\r\n"
    		+ "				\"internalType\": \"address\",\r\n"
    		+ "				\"name\": \"to\",\r\n"
    		+ "				\"type\": \"address\"\r\n"
    		+ "			},\r\n"
    		+ "			{\r\n"
    		+ "				\"internalType\": \"uint256\",\r\n"
    		+ "				\"name\": \"quantity\",\r\n"
    		+ "				\"type\": \"uint256\"\r\n"
    		+ "			}\r\n"
    		+ "		],\r\n"
    		+ "		\"name\": \"transferBatch\",\r\n"
    		+ "		\"outputs\": [],\r\n"
    		+ "		\"stateMutability\": \"nonpayable\",\r\n"
    		+ "		\"type\": \"function\"\r\n"
    		+ "	}\r\n"
    		+ "]";

    protected SupplyChainContract(
            String contractAddress,
            Web3j web3j,
            Credentials credentials,
            ContractGasProvider gasProvider
    ) {
        super(ABI, contractAddress, web3j, credentials, gasProvider);
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
    	System.out.println("Start of batchtransfereventflowable");
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BATCHTRANSFERRED_EVENT));
        System.out.println("Next Return of batchtransfereventflowable");
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
    
    public List<BatchTransferredEventResponse> getPastBatchTransferredEvents(
            DefaultBlockParameter start,
            DefaultBlockParameter end
    ) throws Exception {

        EthFilter filter = new EthFilter(start, end, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BATCHTRANSFERRED_EVENT));

        List<Log> logs = web3j.ethGetLogs(filter)
                .send()
                .getLogs()
                .stream()
                .map(l -> (Log) l.get())
                .toList();

        List<BatchTransferredEventResponse> events = new ArrayList<>();

        for (Log log : logs) {
            EventValues values =
                    staticExtractEventParameters(BATCHTRANSFERRED_EVENT, log);

            BatchTransferredEventResponse e = new BatchTransferredEventResponse();
            e.log = log;
            e.batchHash = (byte[]) values.getIndexedValues().get(0).getValue();
            e.from = values.getIndexedValues().get(1).getValue().toString();
            e.to = values.getIndexedValues().get(2).getValue().toString();
            e.quantity = (BigInteger) values.getNonIndexedValues().get(0).getValue();
            e.timestamp = (BigInteger) values.getNonIndexedValues().get(1).getValue();

            events.add(e);
        }

        return events;
    }


}
