package hello.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		System.setProperty("spring.config.name", "application-core,application");
		SpringApplication.run(ClientApplication.class, args);
	}

}
