package com.traceledger.module.shipment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traceledger.dto.ApiResponse;
import com.traceledger.module.shipment.dto.ShipmentRegisterModel;
import com.traceledger.module.shipment.service.ShipmentService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/shipment")
@Slf4j
public class ShipmentController {

	@Autowired
	private ShipmentService shipService;
	
	@PostMapping("/")
	public ResponseEntity<ApiResponse> createShipment(@RequestBody @Valid ShipmentRegisterModel model){
		shipService.createShipment(model);
		return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true , null , "Shipment Created"));
	}
	
	@PostMapping("/{shipmentId}/dispatch")
	public ResponseEntity<ApiResponse> dispatchShipment(@PathVariable Long shipmentId){
		shipService.dispatchShipment(shipmentId);
		return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true , null , "Shipment Dispatched"));

	}
	
	@PostMapping("/{shipmentId}/receive")
	public ResponseEntity<ApiResponse> receiveShipment(@PathVariable Long shipmentId) {
	    shipService.receiveShipment(shipmentId);
	    return ResponseEntity.ok(new ApiResponse(true, null, "Shipment Received"));
	}

	@PostMapping("/{shipmentId}/cancel")
	public ResponseEntity<ApiResponse> cancelShipment(@PathVariable Long shipmentId) {
	    shipService.cancelShipment(shipmentId);
	    return ResponseEntity.ok(new ApiResponse(true, null, "Shipment Cancelled"));
	}
	
}
