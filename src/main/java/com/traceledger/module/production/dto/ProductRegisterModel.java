package com.traceledger.module.production.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductRegisterModel {
	
	@NotBlank(message="Name can not be blank")
	private String name;
	
	@NotNull(message="MRP can not be null")
	private BigDecimal mrp;
	
	private String description;
}
