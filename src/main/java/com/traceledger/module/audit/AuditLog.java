package com.traceledger.module.audit;

import java.time.LocalDateTime;

import com.traceledger.module.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    @ManyToOne
    private User performedBy;

    private LocalDateTime performedAt;

    private String metadata;
}

