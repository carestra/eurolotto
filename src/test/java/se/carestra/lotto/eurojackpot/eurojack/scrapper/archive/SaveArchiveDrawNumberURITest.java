package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import se.carestra.lotto.eurojackpot.eurojack.scrapper.archive.h2support.H2EmbeddedDataSourceSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SaveArchiveDrawNumberURITest extends H2EmbeddedDataSourceSupport {

  @AfterEach
  void finalizeTest() {
    System.out.print("Just put a break-point (Suspend -> 'Thread') point here.");
    System.out.print("Or anywhere you want to inspect the database.");
  }

  @Test
  @Sql(scripts = {"/draw_number_uri_schema.sql"})
  void saveSingleValue() {
    String pathUrl = "/results/14-06-2025";
    String archiveUrl = "https://euro-jackpot.com/archive" + pathUrl;
    DrawNumberURI saved = repository.save(new DrawNumberURI(pathUrl, archiveUrl));

    assertNotNull(saved);
    assertEquals(pathUrl, saved.path());
    assertEquals(archiveUrl, saved.archiveUrl());
  }
}
