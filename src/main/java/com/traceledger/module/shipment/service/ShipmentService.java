package com.traceledger.module.shipment.service;

import java.util.Optional;

import com.traceledger.module.production.entity.Batch;
import com.traceledger.module.shipment.dto.ShipmentRegisterModel;
import com.traceledger.module.shipment.entity.Shipment;
import com.traceledger.module.shipment.entity.ShipmentItem;
import com.traceledger.module.user.entity.User;

import jakarta.validation.Valid;

public interface ShipmentService {

	void createShipment(@Valid ShipmentRegisterModel model);

	void dispatchShipment(Long shipmentId);

	Shipment getShipmentById(Long shipmentId);

	boolean isShipmentRelatedToUser(Shipment shipment, User user);

	void receiveShipment(Long shipmentId);

	void cancelShipment(Long shipmentId);

	Optional<ShipmentItem> getShipmentItemOptionalByShipmentAndBatch(Shipment shipment, Batch batch);

}
