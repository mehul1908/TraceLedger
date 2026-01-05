package com.traceledger.module.blockchain.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.web3j.abi.EventValues;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.datatypes.Event;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.utils.Numeric;

import java.util.ArrayList;

import com.traceledger.module.blockchain.contract.SupplyChainContract;
import com.traceledger.module.blockchain.dto.BlockchainTamperData;
import com.traceledger.module.blockchain.dto.BlockchainTransferData;
import com.traceledger.module.blockchain.entity.BlockchainTransaction;
import com.traceledger.module.blockchain.entity.BlockchainTxIntent;
import com.traceledger.module.blockchain.repo.BlockchainTransactionRepo;
import com.traceledger.module.blockchain.repo.IntentRepo;
import com.traceledger.module.production.entity.Batch;
import com.traceledger.module.production.service.BatchService;
import com.traceledger.module.shipment.entity.ShipmentItem;
import com.traceledger.module.shipment.repo.ShipmentItemRepo;

@Service
public class BlockchainServiceImpl implements BlockchainService {

    @Autowired
    private SupplyChainContract contract;
    
    @Autowired
    private Web3j web3j;
    
    @Autowired
    private IntentRepo intentRepo;
    
    @Autowired
	private ShipmentItemRepo shipItemRepo;
    
    @Autowired
    private BlockchainTransactionRepo txnRepo;
    
    @Autowired
    private BatchService batchService;

    @Override
    public String transferBatch(
            String batchHash,
            String toWallet,
            int quantity
    ) {
        try {
            TransactionReceipt receipt =
                contract.transferBatch(
                        Numeric.hexStringToByteArray(batchHash),
                        toWallet,
                        BigInteger.valueOf(quantity)
                ).send();

            return receipt.getTransactionHash();

        } catch (Exception e) {
            throw new IllegalStateException("Blockchain transfer failed", e);
        }
    }

    @Override
    public List<BlockchainTamperData> replay() throws Exception {

        // Get earliest known blockchain tx
        BlockchainTxIntent intent =
            intentRepo.findFirstByOrderByCreatedAtAsc()
                      .orElseThrow();

        BigInteger fromBlock =
            web3j.ethGetTransactionReceipt(intent.getTxHash())
                 .send()
                 .getTransactionReceipt()
                 .orElseThrow()
                 .getBlockNumber();

        BigInteger toBlock =
            web3j.ethBlockNumber().send().getBlockNumber();

        List<BlockchainTransferData> transfers =
            scanBlocks(fromBlock, toBlock);
        
        List<BlockchainTamperData> errors = new ArrayList<>();

        for(BlockchainTransferData data : transfers) {
        	Optional<BlockchainTransaction> txnOp = txnRepo.findByTxHash(data.getTxHash());
        	if(txnOp.isEmpty()) {
        		errors.add(new BlockchainTamperData(data.getTxHash(), null,data.getBatchHash() , "Transaction with txHash is not existed"));
        		continue;
        	}
        	BlockchainTransaction txn = txnOp.get();
        	Optional<Batch> batchOp = batchService.getBatchOptionalByBatchHash(data.getBatchHash());
        	if(batchOp.isEmpty()) {
        		errors.add(new BlockchainTamperData(data.getTxHash(), txn.getShipment().getId(), data.getBatchHash(), "Batch with given Batch is not existed"));
        		continue;
        	}
        	Optional<ShipmentItem> itemOp = shipItemRepo.findByShipmentAndBatch(txn.getShipment() , batchOp.get());
        	if(itemOp.isEmpty()) {
        		errors.add(new BlockchainTamperData(data.getTxHash(), txn.getShipment().getId(), data.getBatchHash(), "Shipment Item is not existed"));
        		continue;
        	}
        	if (itemOp.get().getQuantity().intValue() != data.getQuantity()) {
        	    errors.add(new BlockchainTamperData(
        	        data.getTxHash(),
        	        txn.getShipment().getId(),
        	        data.getBatchHash(),
        	        "Quantity does not match"
        	    ));
        	}

        }
        
        return errors;

    }


    private List<BlockchainTransferData> scanBlocks(
            BigInteger fromBlock,
            BigInteger toBlock
    ) throws Exception {

        String contractAddress =
                contract.getContractAddress().toLowerCase();

        Event event = SupplyChainContract.BATCHTRANSFERRED_EVENT;

        EthFilter filter = new EthFilter(
                new DefaultBlockParameterNumber(fromBlock),
                new DefaultBlockParameterNumber(toBlock),
                contractAddress
        );

        // Event signature topic
        filter.addSingleTopic(EventEncoder.encode(event));

        EthLog ethLog =
                web3j.ethGetLogs(filter).send();

        List<BlockchainTransferData> results = new ArrayList<>();

        for (EthLog.LogResult<?> logResult : ethLog.getLogs()) {

            Log log = (Log) logResult.get();

            EventValues values =
                    Contract.staticExtractEventParameters(event, log);

            if (values == null) continue;

            String txHash =
                    log.getTransactionHash().toLowerCase();

            byte[] batchHashBytes =
                    (byte[]) values.getIndexedValues()
                                   .get(0)
                                   .getValue();

            String batchHash =
                    Numeric.toHexString(batchHashBytes);

            BigInteger quantityBI =
                    (BigInteger) values.getNonIndexedValues()
                                       .get(0)
                                       .getValue();

            results.add(
                    new BlockchainTransferData(
                            txHash,
                            batchHash,
                            quantityBI.intValueExact(),
                            log.getBlockNumber()
                    )
            );
        }

        return results;
    }


}

