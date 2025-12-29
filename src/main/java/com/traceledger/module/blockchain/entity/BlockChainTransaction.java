package com.traceledger.module.blockchain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "blockchain_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockChainTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String txHash;

    @Column(nullable = false)
    private String entityType; // BATCH / SHIPMENT

    @Column(nullable = false)
    private String entityHash;

    @Enumerated(EnumType.STRING)
    private TxStatus status;

    private String errorMessage;
}

