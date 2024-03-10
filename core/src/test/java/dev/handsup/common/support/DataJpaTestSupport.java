package dev.handsup.common.support;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;

import dev.handsup.common.config.QueryDslConfig;
import dev.handsup.common.config.RedisTestConfig;
import dev.handsup.support.DatabaseCleaner;
import dev.handsup.support.DatabaseCleanerExtension;
import dev.handsup.support.TestContainerSupport;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Repository.class))
@Import({DatabaseCleaner.class, QueryDslConfig.class, RedisTestConfig.class})
@AutoConfigureTestDatabase(replace = NONE)
@ExtendWith(DatabaseCleanerExtension.class)
public abstract class DataJpaTestSupport extends TestContainerSupport {
}
