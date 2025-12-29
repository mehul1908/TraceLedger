package com.traceledger.module.production.entity;

import java.time.LocalDate;

import com.traceledger.module.production.enums.BatchStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="batches")
@Builder
public class Batch {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable=false, unique=true)
	private String batchNo;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Product product;
	
	@ManyToOne
	@JoinColumn(nullable=false)
	private Factory factory;
	
	@Column(nullable = false, unique = true, length = 66)
	private String batchHash;
	
	@Column(nullable=false)
	private LocalDate manufactureDate;
	
	@Column(nullable=false)
	private Integer quantity;
	
	@Enumerated(EnumType.STRING)
	@Builder.Default
	@Column(nullable = false)
	private BatchStatus status = BatchStatus.CREATED;
	
	
	
}