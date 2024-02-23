package dev.handsup.support;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;

import dev.handsup.common.config.QueryDslConfig;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Repository.class))
@Import({DatabaseCleaner.class, QueryDslConfig.class})
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(DatabaseCleanerExtension.class)
public abstract class DataJpaTestSupport extends TestContainerSupport {

	@Autowired
	protected TestEntityManager em;
}
