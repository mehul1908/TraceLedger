package com.traceledger.module.shipment.entity;

import com.traceledger.module.production.entity.Batch;
import com.traceledger.module.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "shipments")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Batch batch;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(optional = false)
    private User fromUser;

    @ManyToOne(optional = false)
    private User toUser;

    @ManyToOne(optional=false)
    private User transporter;
    
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    @Column(length = 66)
    private String shipmentHash;

    @Column(length = 66)
    private String blockchainTxHash;
}

