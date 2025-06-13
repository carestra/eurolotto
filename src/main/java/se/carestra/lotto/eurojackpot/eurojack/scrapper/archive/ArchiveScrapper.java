package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public interface ArchiveScrapper {

  Logger LOG = LoggerFactory.getLogger(ArchiveScrapper.class);

  Optional<List<DrawNumberURI>> fetch(String archiveUrl);

  static Element selectFirstFromElementOrNull(Document document, String cssQuery) {
    Elements tableElements = document.select(cssQuery);
    if (tableElements != null && !tableElements.isEmpty()) {
      return tableElements.getFirst();
    } else {
      LOG.warn("Could not find cssQuery [{}] in document [{}]", cssQuery, document);
      return null;
    }
  }

  static Element selectFirstFromElementOrNull(Element element, String cssQuery) {
    Elements tableElements = element.select(cssQuery);
    if (tableElements != null && !tableElements.isEmpty()) {
      return tableElements.getFirst();
    } else {
      LOG.warn("Could not find cssQuery [{}] in element [{}]", cssQuery, element);
      return null;
    }
  }
}
