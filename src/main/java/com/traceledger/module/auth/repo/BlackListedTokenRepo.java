package com.traceledger.module.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traceledger.module.auth.entity.BlackListedToken;



@Repository
public interface BlackListedTokenRepo extends JpaRepository<BlackListedToken, String>{

}
