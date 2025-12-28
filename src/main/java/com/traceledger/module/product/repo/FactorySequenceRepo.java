package com.traceledger.module.product.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.traceledger.module.product.entity.FactorySequence;

@Repository
public interface FactorySequenceRepo extends JpaRepository<FactorySequence, Long> {

    @Modifying
    @Query(value = "INSERT INTO factory_sequence VALUES ()", nativeQuery = true)
    void insertDummyRow();

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();
}


