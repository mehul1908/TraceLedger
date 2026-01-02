package com.traceledger.module.shipment.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentRegisterModel {
	
	@NotNull(message="From User can not be null")
	private Long fromUserId;
	
	@NotNull(message="To User can not be null")
	private Long toUserId;
	
	@NotNull(message="Transporter can not be null")
	private Long transporterId;
	
	@NotBlank(message = "Vehicle Number can not be null")
	private String vehicleNo;
	
	@NotNull(message = "Shipment Item can not be null")
	private List<ShipmentItemRegisterModel> items;
}
