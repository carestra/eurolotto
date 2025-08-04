package se.carestra.lotto.eurojackpot.eurojack.archive.repository;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURI;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SaveArchiveDrawNumberURITest extends H2EmbeddedDataSourceSupport {

  @Test
  @Sql(scripts = {"/draw_resource_uri_schema.sql"})
  void saveAndFetchSaved() {
    String pathUrl = "/results/14-06-2025";
    String archiveUrl = "http://mock.server.com/" + pathUrl;
    DrawNumberURI uri = new DrawNumberURI(pathUrl, archiveUrl);
    List<DrawNumberURI> uris = List.of(uri);
    repository.saveAll(uris);
    Optional<DrawNumberURI> saved = repository.findById(uri.drawDate());

    assertTrue(saved.isPresent());
    assertEquals(pathUrl, saved.get().resourceUri());
    assertEquals(archiveUrl, saved.get().archiveUrl());
  }

}
