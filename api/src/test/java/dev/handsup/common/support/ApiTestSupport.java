package dev.handsup.common.support;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.handsup.support.DatabaseCleaner;
import dev.handsup.support.DatabaseCleanerExtension;
import dev.handsup.support.TestContainerSupport;

@SpringBootTest
@AutoConfigureMockMvc
@Import(DatabaseCleaner.class)
@ExtendWith(DatabaseCleanerExtension.class)
public abstract class ApiTestSupport extends TestContainerSupport {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	protected String toJson(Object object) throws JsonProcessingException {
		return objectMapper.writeValueAsString(object);
	}
}
