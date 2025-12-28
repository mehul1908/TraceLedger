package com.traceledger.module.product.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactoryRegisterModel {

	@NotBlank(message = "Name can not be blank")
	private String name;
	
	@NotBlank(message = "Location can not be blank")
	private String location;
	
}
