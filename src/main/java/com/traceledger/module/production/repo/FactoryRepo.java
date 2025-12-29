package com.traceledger.module.production.repo;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traceledger.module.production.entity.Factory;

@Repository
public interface FactoryRepo extends JpaRepository<Factory, Long> {

	Optional<Factory> findByFactoryCode(String factoryCode);

}
