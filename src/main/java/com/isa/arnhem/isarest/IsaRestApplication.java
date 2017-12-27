package com.isa.arnhem.isarest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IsaRestApplication {
	public static final String DATABASE_NAME = "isa-rest";

	public static void main(String[] args) {
		SpringApplication.run(IsaRestApplication.class, args);
	}
}
