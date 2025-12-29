package com.traceledger.module.production.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchRegisterModel {
	private String productCode;
	
	private String factoryCode;
	
	private Integer quantity;
	
}
