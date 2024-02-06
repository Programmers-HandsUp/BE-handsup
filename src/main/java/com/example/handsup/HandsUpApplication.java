package com.example.handsup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HandsUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(HandsUpApplication.class, args);
	}

}
