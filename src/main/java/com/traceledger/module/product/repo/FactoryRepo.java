package com.traceledger.module.product.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traceledger.module.product.entity.Factory;

@Repository
public interface FactoryRepo extends JpaRepository<Factory, Long> {

}
