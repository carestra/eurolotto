package se.carestra.lotto.eurojackpot.eurojack.archive.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
class DataSourceTestConfiguration {

  /**
   * ### To access the database use ####
   * Saved Settings: Generic H2 (Embedded)
   * Driver Class : org.h2.Driver
   * JDBC URL     : jdbc:h2:mem:inmemory_test_db
   * Usr Name     : sa
   * Password     :
   */
  @Bean
  DataSource dataSource() {
    return new EmbeddedDatabaseBuilder().
        setType(EmbeddedDatabaseType.H2)
        .setName("inmemory_test_db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DEFAULT_NULL_ORDERING=HIGH")
        .build();
  }

  @Bean
  JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}
