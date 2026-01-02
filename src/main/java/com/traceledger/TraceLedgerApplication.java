package com.traceledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TraceLedgerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TraceLedgerApplication.class, args);
	}

}
