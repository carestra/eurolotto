package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURI;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURIScrapper;
import se.carestra.lotto.eurojackpot.eurojack.scrapper.api.JsoupScrapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
class EurojackpotDrawNumberURIScrapper implements DrawNumberURIScrapper {
  private static final Logger LOGGER = LoggerFactory.getLogger(EurojackpotDrawNumberURIScrapper.class);

  private final JsoupScrapper scrapper;
  private final String baseUrl;
  private final String uriPrefix;

  EurojackpotDrawNumberURIScrapper(@Value("${eurojackpot.url.base}") String baseUrl,
                                   @Value("${eurojackpot.url.archive.year.prefix}") String uriPrefix,
                                   @Autowired JsoupScrapper scrapper) {
    this.scrapper = scrapper;
    this.baseUrl = baseUrl;
    this.uriPrefix = uriPrefix;
  }

  @Override
  public Optional<List<DrawNumberURI>> fetch(@NonNull String archiveYear) {
    String fullPath = baseUrl + uriPrefix + archiveYear;
    try {
      Optional<Document> document = scrapper.getDocument(fullPath);

      return Optional.ofNullable(document)
          .flatMap(optionalDocument -> {
            // TODO: verify that year has been selected
            SelectedYearElement yearSelected = new SelectedYearElement(optionalDocument);
            // TODO: Use builder pattern?
            TableElements table = new TableElements(optionalDocument);
            TableBodyElement body = new TableBodyElement(table.getTableElements());
            TableBodyRowElements rows = new TableBodyRowElements(body.getTableBody());
            TableBodyRowDataElement data = new TableBodyRowDataElement(rows.getTableBodyRowElements());
            AnchorElements anchors = new AnchorElements(data.getRowsDataStream());
            HrefElements hrefs = new HrefElements(anchors.getAnchorsStream());
            return hrefs.getDrawNumberURIs(fullPath);
          });
    } catch (IOException e) {
      LOGGER.error("Could not fetch draw numbers detail url detailUri from archive [{}]", fullPath, e);
      return Optional.empty();
    }
  }
}
