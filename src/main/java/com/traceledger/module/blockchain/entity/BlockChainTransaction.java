package com.traceledger.module.blockchain.entity;

import java.time.LocalDateTime;

import com.traceledger.module.shipment.entity.Shipment;
import com.traceledger.module.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "blockchain_transactions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlockchainTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String txHash;

    private String batchHash;

    private Integer quantity;

    @ManyToOne
    private Shipment shipment;

    @ManyToOne
    private User fromUser;

    @ManyToOne
    private User toUser;

    private LocalDateTime createdAt;
}
