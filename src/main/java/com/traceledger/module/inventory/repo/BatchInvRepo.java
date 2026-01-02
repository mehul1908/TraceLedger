package com.traceledger.module.inventory.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traceledger.module.inventory.entity.BatchInventory;
import com.traceledger.module.production.entity.Batch;
import com.traceledger.module.user.entity.User;

@Repository
public interface BatchInvRepo extends JpaRepository<BatchInventory, Long>{

	Optional<BatchInventory> findByBatchAndOwner(Batch batch, User owner);

}
