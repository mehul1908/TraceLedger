package com.traceledger.module.inventory.entity;

import com.traceledger.module.production.entity.Batch;
import com.traceledger.module.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "batch_inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Batch batch;

    @ManyToOne(optional = false)
    private User owner;

    @Column(nullable = false)
    private Integer availableQuantity;
    
    @Column(nullable = false)
    @Builder.Default
    private Integer reservedQuantity = 0;
    
    /**
     * Optimistic locking version field. Automatically incremented on update.
     */
    @Version
    private Long version;
}

