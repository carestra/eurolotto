package se.carestra.lotto.eurojackpot.eurojack.archive.repository;

import org.h2.tools.Server;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import java.sql.SQLException;

/**
 * Uses the configuration from DataSourceTestConfiguration.
 * <p>
 * Cannot use @JdbcTest since embedded db is hard coded in H2EmbeddedDatabaseConfigurer.class.
 * Using SpringBootTest instead.
 * See https://stackoverflow.com/questions/77130082/how-do-i-configure-h2-used-in-jdbctest
 * <p>
 * See also https://stackoverflow.com/questions/12390116/access-to-h2-web-console-while-running-junit-test-in-a-spring-application
 */
@SpringBootTest
@Import(DrawNumberURIRepository.class)
@ContextConfiguration(classes = DataSourceTestConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class H2EmbeddedDataSourceSupport {

  @Autowired
  protected DrawNumberURIRepository repository;

  /***
   * Starts a h2-server on port 8082
   * See DataSourceTestConfiguration for connection string.
   *
   * To log in to console, go to: http://localhost:8082/
   * To access it, set a break-point anywhere in your test code or where you access the datasource.
   * Make the breakpoint suspend type to "Thread", not "ALL"
   * Right-click on the breakpoint and select suspend -> "Thread"
   */
  @BeforeAll
  static void setup() throws SQLException {
    Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
  }
}
