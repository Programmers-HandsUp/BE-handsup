package dev.handsup.common.config;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FCMConfig {

	@Value("${fcm.certification}")
	private String googleApplicationCredentials;

	@PostConstruct
	public void initialize() throws IOException {
		ClassPathResource resource = new ClassPathResource(googleApplicationCredentials);

		try (InputStream inputStream = resource.getInputStream()) {
			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(inputStream))
				.build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
				log.info("FirebaseApp initialization complete");
			}
		}
	}

	@Bean
	public FirebaseMessaging firebaseMessaging() {
		return FirebaseMessaging.getInstance();
	}
}
