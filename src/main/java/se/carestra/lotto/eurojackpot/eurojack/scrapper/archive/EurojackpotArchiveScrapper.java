package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.carestra.lotto.eurojackpot.eurojack.scrapper.JsoupScrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class EurojackpotArchiveScrapper implements ArchiveScrapper {
  private final Logger logger = LoggerFactory.getLogger(EurojackpotArchiveScrapper.class);

  private final JsoupScrapper scrapper;

  public EurojackpotArchiveScrapper(@Autowired JsoupScrapper scrapper) {
    this.scrapper = scrapper;
  }

  @Override
  public Optional<List<DrawNumberURI>> fetch(String archiveUrl) {
    List<DrawNumberURI> drawNumberURIs;
    try {
      drawNumberURIs = new ArrayList<>();
      Optional<Document> document = scrapper.getDocument(archiveUrl);

      return document
          .map(doc -> {
            Elements buttonElements = doc.select("a.btn ");
            buttonElements.removeIf(button -> button.hasClass("btn--grey"));
            return extractHRefOrNull(doc, buttonElements, archiveUrl);
          })
          .map(doc -> ArchiveScrapper.selectFirstFromElementOrNull(doc, "table"))
          .map(tableElement -> ArchiveScrapper.selectFirstFromElementOrNull(tableElement, "tbody"))
          .map(bodyElement -> bodyElement.select("tr"))
          .map(rows -> {
            rows.stream().forEach(row -> {
              Element data = row.selectFirst("td");
              if (data != null) {
                Element anchor = ArchiveScrapper.selectFirstFromElementOrNull(data, "a");
                addToResultIfPresent(drawNumberURIs, data, anchor, archiveUrl);
              } else {
                if (logger.isWarnEnabled()) {
                  logger.warn("Could not find data row from [{}]", rows);
                }
              }
            });

            return drawNumberURIs.isEmpty() ? null : drawNumberURIs;
          });
    } catch (IOException e) {
      logger.error("Could not fetch draw numbers from archive [{}]", archiveUrl, e);
      return Optional.empty();
    }
  }

  private void addToResultIfPresent(List<DrawNumberURI> drawNumberURIs, Element data, Element anchor, String archiveUrl) {
    if (anchor != null) {
      DrawNumberURI
          .parse(anchor.attr("href"), archiveUrl)
          .ifPresent(drawNumberURIs::add);
    } else {
      if (logger.isWarnEnabled()) {
        logger.warn(String.format("Unable to find anchor in data row %s", data));
      }
    }
  }

  private Document extractHRefOrNull(Document doc, Elements buttonElements, String archiveUrl) {
    String uriPath = buttonElements.attr("href");
    if (uriPath != null && archiveUrl.contains(uriPath)) {
      return doc;
    } else {
      if (logger.isWarnEnabled()) {
        logger.warn(String.format("Selected archive year[%s] is not the same as the requested [%s]", uriPath, archiveUrl));
      }
      return null;
    }
  }
}
