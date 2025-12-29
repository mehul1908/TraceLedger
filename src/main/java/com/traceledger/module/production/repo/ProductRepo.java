package com.traceledger.module.production.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traceledger.module.production.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

	Optional<Product> findByProductCode(String productCode);

}
