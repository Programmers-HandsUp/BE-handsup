package dev.handsup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HandsUpApplication {

	public static void main(String[] args) {
		// 타 모듈 yml 가져오기
		System.setProperty("spring.config.name", "application, application-core");
		SpringApplication.run(HandsUpApplication.class, args);
	}
}
