package com.traceledger.module.shipment.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.traceledger.module.shipment.enums.ShipmentStatus;
import com.traceledger.module.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn( nullable = false)
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn( nullable = false)
    private User toUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false)
    private User transporter;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ShipmentStatus status = ShipmentStatus.CREATED;

    @Column(length = 66)
    private String shipmentHash;

    @Column(nullable = false)
    private String vehicleNo;

    @Builder.Default
    private LocalDateTime creationTime = LocalDateTime.now();

    private LocalDateTime receivedTime;

    @OneToMany(mappedBy = "shipment")
    private List<ShipmentItem> items;
}
