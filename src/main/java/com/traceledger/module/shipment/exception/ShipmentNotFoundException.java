package com.traceledger.module.shipment.exception;

public class ShipmentNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 596222247897199281L;

	public ShipmentNotFoundException() {
		super("Shipment not found");
	}
	
	public ShipmentNotFoundException(Long shipmentId) {
		super("Shipment with id "+shipmentId+" is not found");
	}
}
