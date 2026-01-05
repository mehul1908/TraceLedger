package com.traceledger.module.production.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traceledger.module.production.entity.Batch;

@Repository
public interface BatchRepo extends JpaRepository<Batch, Long> {

	Optional<Batch> findByBatchHash(String batchHash);

}
