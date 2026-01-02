package com.traceledger.module.blockchain.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traceledger.module.blockchain.entity.BlockchainTransaction;

@Repository
public interface BlockchainTransactionRepo extends JpaRepository<BlockchainTransaction, Long>{

}
