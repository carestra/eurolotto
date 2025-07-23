package se.carestra.lotto.eurojackpot.eurojack.archive.repository;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SaveArchiveDrawNumberURITest extends H2EmbeddedDataSourceSupport {

  @Test
  @Sql(scripts = {"/draw_resource_uri_schema.sql"})
  void saveSingleValue() {
    String pathUrl = "/results/14-06-2025";
    String archiveUrl = "http://mock.server.com/" + pathUrl;
    DrawNumberURI saved = repository.save(new DrawNumberURI(pathUrl, archiveUrl));

    assertNotNull(saved);
    assertEquals(pathUrl, saved.resourceUri());
    assertEquals(archiveUrl, saved.archiveUrl());
  }
}
