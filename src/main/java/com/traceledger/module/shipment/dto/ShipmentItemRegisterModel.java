package com.traceledger.module.shipment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentItemRegisterModel {
	
	@NotNull(message="Quantity can not be null")
	private Integer quantity;
	
	@NotNull(message="Batch can not be null")
	private Long batchId;
}
