package dev.handsup.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:3000") // 허용할 출처 : 배포할 때는 특정 출처로 변경
			.allowedMethods("*")
			.allowCredentials(true) // 쿠키 인증 요청 허용
			.maxAge(3000); // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱
	}
}