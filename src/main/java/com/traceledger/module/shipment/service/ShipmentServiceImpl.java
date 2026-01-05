package com.traceledger.module.shipment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traceledger.exception.UnauthorizedUserException;
import com.traceledger.module.audit.enums.AuditAction;
import com.traceledger.module.audit.service.AuditLogService;
import com.traceledger.module.blockchain.entity.BlockchainTxIntent;
import com.traceledger.module.blockchain.enums.IntentStatus;
import com.traceledger.module.blockchain.repo.IntentRepo;
import com.traceledger.module.blockchain.service.BlockchainService;
import com.traceledger.module.inventory.entity.BatchInventory;
import com.traceledger.module.inventory.service.BatchInventoryService;
import com.traceledger.module.production.entity.Batch;
import com.traceledger.module.production.service.BatchService;
import com.traceledger.module.shipment.dto.ShipmentItemRegisterModel;
import com.traceledger.module.shipment.dto.ShipmentRegisterModel;
import com.traceledger.module.shipment.entity.Shipment;
import com.traceledger.module.shipment.entity.ShipmentItem;
import com.traceledger.module.shipment.enums.ShipmentStatus;
import com.traceledger.module.shipment.exception.ShipmentNotFoundException;
import com.traceledger.module.shipment.repo.ShipmentItemRepo;
import com.traceledger.module.shipment.repo.ShipmentRepo;
import com.traceledger.module.user.entity.User;
import com.traceledger.module.user.enums.UserRole;
import com.traceledger.module.user.service.UserService;
import com.traceledger.util.HashUtil;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {

	@Autowired
	private ShipmentRepo shipRepo;
	
	@Autowired
	private ShipmentItemRepo shipItemRepo;

	@Autowired
	private UserService userService;
	
	@Autowired
	private BatchService batchService;
	
	@Autowired
	private BatchInventoryService batchInvService;
	
	@Autowired
	private AuditLogService auditService;
	
	@Autowired
	private BlockchainService blockchainService;
	
	@Autowired
	private IntentRepo intentRepo;
	
	
	@Override
	@Transactional
	public void createShipment(@Valid ShipmentRegisterModel model) {
		
		User fromUser = userService.getUserById(model.getFromUserId());
		User toUser = userService.getUserById(model.getToUserId());
		User transporter = userService.getUserById(model.getTransporterId());
		
		if(!fromUser.getRole().canTransferTo(toUser.getRole()) || transporter.getRole() != UserRole.ROLE_TRANSPORTER) {
			throw new IllegalStateException("Invalid User or Transporter");
		}
		
		if (fromUser.getRole() == UserRole.ROLE_RETAILER) {
		    throw new IllegalStateException("Retailer cannot create outbound shipments");
		}
		
		if (fromUser.getId().equals(toUser.getId())) {
		    throw new IllegalStateException("From and To user cannot be the same");
		}

		
		LocalDateTime time = LocalDateTime.now();
		
		String canonical = String.join("|",
			    fromUser.getId().toString(),
			    toUser.getId().toString(),
			    transporter != null ? transporter.getId().toString() : "NA",
			    model.getVehicleNo(),
			    time.toString()
			    );

		String shipmentHash = HashUtil.sha256(canonical);
		
		Shipment shipment = Shipment.builder()
				.fromUser(fromUser)
				.toUser(toUser)
				.transporter(transporter)
				.vehicleNo(model.getVehicleNo())
				.shipmentHash(shipmentHash)
				.creationTime(time)
				.build();
		
		shipRepo.save(shipment);
		
		List<ShipmentItemRegisterModel> list = model.getItems();
		
		for(ShipmentItemRegisterModel item : list) {
			Batch batch = batchService.getBatchById(item.getBatchId());
			BatchInventory batchInv = batchInvService.findBatchInvByBatchAndOwner(batch , fromUser);
			if(item.getQuantity() <= batchInv.getAvailableQuantity()) {
				ShipmentItem shipItem = ShipmentItem.builder()
						.batch(batch)
						.quantity(item.getQuantity())
						.shipment(shipment)
						.build();
				shipItemRepo.save(shipItem);
				int available = batchInv.getAvailableQuantity();
				int reserved = batchInv.getReservedQuantity();
				batchInv.setAvailableQuantity(available - item.getQuantity());
				batchInv.setReservedQuantity(reserved + item.getQuantity());
				batchInvService.save(batchInv);
			}else {
				throw new IllegalStateException("Quantity to ship is greater than available quantity. Batch : " + batch.getBatchNo());
			}
		}
		
		auditService.create(AuditAction.SHIPMENT_CREATED, fromUser, "Shipment Created : " + shipment.getId());
		
	}

	@Override
	@Transactional
	public void dispatchShipment(Long shipmentId) {

	    Shipment shipment = getShipmentById(shipmentId);

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

	    if(shipment.getStatus() != ShipmentStatus.CREATED) {
			throw new IllegalStateException("Shipment cannot be dispatched");
		}
		
	    if (auth == null || !(auth.getPrincipal() instanceof User user)) {
	        throw new UnauthorizedUserException("User is unauthenticated");
	    }
	    log.warn(user.getId().toString());
	    
	    if(!this.isShipmentRelatedToUser(shipment , user)) {
	    	throw new UnauthorizedUserException("User is not related to entity");
	    }
	    
	    
	    
	    if(user.getRole() != UserRole.ROLE_TRANSPORTER) {
	    	throw new UnauthorizedUserException("You can not dispatch a Shipment");
	    }


	    shipment.setStatus(ShipmentStatus.DISPATCH_PENDING);
	    shipRepo.save(shipment);

	    for (ShipmentItem item : shipment.getItems()) {

	        Batch batch = item.getBatch();

	     // 1️⃣ Create intent FIRST (without tx hash)
	        BlockchainTxIntent intent = BlockchainTxIntent.builder()
	                .txHash(null) // or "PENDING"
	                .batchHash(batch.getBatchHash())
	                .quantity(item.getQuantity())
	                .shipment(shipment)
	                .fromUser(shipment.getFromUser())
	                .toUser(shipment.getToUser())
	                .status(IntentStatus.PENDING)
	                .createdAt(LocalDateTime.now())
	                .build();

	        intentRepo.save(intent);

	        // 2️⃣ Send blockchain transaction
	        String txHash = blockchainService.transferBatch(
	                batch.getBatchHash(),
	                shipment.getToUser().getWalletAddress(),
	                item.getQuantity()
	        ).toLowerCase();

	        // 3️⃣ Update intent with real tx hash
	        intent.setTxHash(txHash);
	        intentRepo.save(intent);

	    }

	    auditService.create(
	            AuditAction.SHIPMENT_DISPATCH_INITIATED,
	            shipment.getFromUser(),
	            "Shipment dispatch initiated: " + shipment.getId()
	    );
	}

	@Override
	@Transactional
	public void receiveShipment(Long shipmentId) {

	    Shipment shipment = getShipmentById(shipmentId);

	    Authentication auth =
	            SecurityContextHolder.getContext().getAuthentication();

	    if (auth == null || !(auth.getPrincipal() instanceof User user)) {
	        throw new UnauthorizedUserException("User is unauthenticated");
	    }

	    // Shipment must already be dispatched
	    if (shipment.getStatus() != ShipmentStatus.DISPATCHED) {
	        throw new IllegalStateException("Shipment is not dispatched");
	    }

	    // Only receiver can confirm
	    if (!shipment.getToUser().getId().equals(user.getId())) {
	        throw new UnauthorizedUserException("Only receiver can receive shipment");
	    }

	    List<ShipmentItem> items = shipment.getItems();
	    for(ShipmentItem item : items) {
	    	Batch batch = item.getBatch();
	    	BatchInventory batchInvFrom = batchInvService.findBatchInvByBatchAndOwner(batch, shipment.getFromUser());
	    	BatchInventory batchInvTo = batchInvService.findBatchInvByBatchAndOwnerIfAbsentCreate(batch , shipment.getToUser());
	    	
	    	batchInvFrom.setReservedQuantity(batchInvFrom.getReservedQuantity() - item.getQuantity());
	    	batchInvTo.setAvailableQuantity(batchInvTo.getAvailableQuantity()+item.getQuantity());
	    	batchInvService.save(batchInvTo);
	    	batchInvService.save(batchInvFrom);
	    	
	    }
	    
	    // Finalize shipment
	    shipment.setStatus(ShipmentStatus.RECEIVED);
	    shipment.setReceivedTime(LocalDateTime.now());
	    shipRepo.save(shipment);

	    auditService.create(
	            AuditAction.SHIPMENT_RECEIVED,
	            user,
	            "Shipment received: " + shipment.getId()
	    );
	}

	@Override
	@Transactional
	public void cancelShipment(Long shipmentId) {

	    Shipment shipment = getShipmentById(shipmentId);

	    Authentication auth =
	            SecurityContextHolder.getContext().getAuthentication();

	    if (auth == null || !(auth.getPrincipal() instanceof User user)) {
	        throw new UnauthorizedUserException("User is unauthenticated");
	    }

	    // Only creator or sender can cancel
	    if (!shipment.getFromUser().getId().equals(user.getId())) {
	        throw new UnauthorizedUserException("You cannot cancel this shipment");
	    }

	    // Disallow cancellation after blockchain confirmation
	    if (shipment.getStatus() == ShipmentStatus.DISPATCHED ||
	        shipment.getStatus() == ShipmentStatus.RECEIVED) {
	        throw new IllegalStateException("Shipment cannot be cancelled");
	    }

	    for(ShipmentItem item : shipment.getItems()) {
	    	Batch batch = item.getBatch();
			BatchInventory batchInv = batchInvService.findBatchInvByBatchAndOwner(batch , user);
			int available = batchInv.getAvailableQuantity();
			int reserved = batchInv.getReservedQuantity();
			batchInv.setAvailableQuantity(available + item.getQuantity());
			batchInv.setReservedQuantity(reserved - item.getQuantity());
			batchInvService.save(batchInv);
	    }
	    
	    shipment.setStatus(ShipmentStatus.CANCELLED);
	    shipRepo.save(shipment);

	    // Mark pending intents as FAILED
	    intentRepo.findByShipmentAndStatus(
	            shipment, IntentStatus.PENDING
	    ).forEach(intent -> {
	        intent.setStatus(IntentStatus.FAILED);
	        intentRepo.save(intent);
	    });

	    auditService.create(
	            AuditAction.SHIPMENT_CANCELLED,
	            user,
	            "Shipment cancelled: " + shipment.getId()
	    );
	}

	@Override
	public Shipment getShipmentById(Long shipmentId) {
		Optional<Shipment> shipOp = shipRepo.findById(shipmentId);
		if(shipOp.isPresent()) return shipOp.get();
		throw new ShipmentNotFoundException(shipmentId);
	}
	
	@Override
	public boolean isShipmentRelatedToUser(Shipment shipment, User user) {
	    Long userId = user.getId();

	    return shipment.getFromUser().getId().equals(userId)
	        || shipment.getToUser().getId().equals(userId)
	        || shipment.getTransporter().getId().equals(userId);
	}

	@Override
	public Optional<ShipmentItem> getShipmentItemOptionalByShipmentAndBatch(Shipment shipment, Batch batch) {
		return shipItemRepo.findByShipmentAndBatch(shipment , batch);
	}

	
}
