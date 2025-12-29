package com.traceledger.module.production.entity;

import java.math.BigDecimal;

import com.traceledger.module.production.enums.ProductStatus;

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
@Table(name="products")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "product_code", nullable = false, unique = true , length = 10)
    private String productCode; // P01, P02, ...
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false , precision =10 , scale = 2 )
	private BigDecimal mrp;
	
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private ProductStatus status = ProductStatus.ACTIVE;
	
	@Column(name = "product_hash", nullable = false, unique = true, length = 66)
	private String productHash;
	
	@Column(length = 255)
	private String description;
}
