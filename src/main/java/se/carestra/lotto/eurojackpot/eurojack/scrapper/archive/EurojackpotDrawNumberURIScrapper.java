package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import se.carestra.lotto.eurojackpot.eurojack.scrapper.JsoupScrapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
public class EurojackpotDrawNumberURIScrapper implements DrawNumberURIScrapper {
  private static final Logger LOGGER = LoggerFactory.getLogger(EurojackpotDrawNumberURIScrapper.class);

  private final JsoupScrapper scrapper;
  private final String baseUrl;
  private final String uriPrefix;

  public EurojackpotDrawNumberURIScrapper(@Value("${eurojackpot.url.base}") String baseUrl,
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

      return findDrawTableRows(archiveYear, document)
          .map(mapTableRowHrefToDrawNumberURI(fullPath))
          .filter(result -> !result.isEmpty());
    } catch (IOException e) {
      LOGGER.error("Could not fetch draw numbers detail url detailUri from archive [{}]", fullPath, e);
      return Optional.empty();
    }
  }

  private static Function<Elements, List<DrawNumberURI>> mapTableRowHrefToDrawNumberURI(String fullPath) {
    return rows ->
        selectAllHrefForEveryTableRowData(rows)
            .map(href -> new DrawNumberURI(href, fullPath))
            .toList();
  }

  private static Stream<String> selectAllHrefForEveryTableRowData(Elements rows) {
    return selectAllAnchorsForEveryTableRowData(rows)
        .map(anchor -> anchor.attr("href"))
        .filter(href -> href != null && !href.isEmpty());
  }

  private static Stream<Element> selectAllAnchorsForEveryTableRowData(Elements rows) {
    return selectAllTableRowData(rows)
        .map(row -> row.select("a"))
        .filter(anchors -> anchors != null && !anchors.isEmpty())
        .map(Elements::getFirst)
        .filter(Objects::nonNull);
  }

  private static Stream<Element> selectAllTableRowData(Elements rows) {
    return rows.stream()
        .map(row -> row.selectFirst("td"))
        .filter(Objects::nonNull);
  }

  private Optional<Elements> findDrawTableRows(String archiveYear, Optional<Document> document) {
    return findDrawTableBody(document, archiveYear)
        .map(bodyElement -> bodyElement.select("tr"));
  }

  private Optional<Element> findDrawTableBody(Optional<Document> document, String archiveYear) {
    return findResultDrawTable(document, archiveYear)
        .map(selectTableBodyElement("tbody"))
        .filter(tableBodyElement -> tableBodyElement.exist() == ElementSelectResult.FOUND)
        .flatMap(TableBodyElement::elementOptional);
  }

  private Function<Element, TableBodyElement> selectTableBodyElement(String cssQuery) {
    return element -> {
      Elements tableElements = element.select(cssQuery);
      if (tableElements != null && !tableElements.isEmpty()) {
        return new TableBodyElement(ElementSelectResult.FOUND, Optional.ofNullable(tableElements.getFirst()));
      } else {
        return new TableBodyElement(ElementSelectResult.NOT_FOUND, Optional.empty());
      }
    };
  }

  private Optional<Element> findResultDrawTable(Optional<Document> document, String archiveYear) {
    return findDocumentForSelectedYear(document, archiveYear)
        .map(findTableElement("table"))
        .filter(drawTable -> drawTable.exist() == ElementSelectResult.FOUND)
        .flatMap(DrawTableElement::optionalTableElement);
  }

  private Function<Element, DrawTableElement> findTableElement(String cssQuery) {
    return element -> {
      Elements tableElements = element.select(cssQuery);
      if (tableElements != null && !tableElements.isEmpty()) {
        return new DrawTableElement(ElementSelectResult.FOUND, Optional.ofNullable(tableElements.getFirst()));
      } else {
        return new DrawTableElement(ElementSelectResult.NOT_FOUND, Optional.empty());
      }
    };
  }

  private Optional<Document> findDocumentForSelectedYear(Optional<Document> document, String archiveYear) {
    return document
        .map(findEnabledSelectedYearButton(archiveYear))
        .filter(selectedYearDocument -> selectedYearDocument.button() == ElementSelectResult.FOUND)
        .map(SelectedYearDocument::document);
  }

  private Function<Document, SelectedYearDocument> findEnabledSelectedYearButton(String archiveYear) {
    return document -> {
      Elements buttonElements = document.select("a.btn ");
      buttonElements.removeIf(button -> button.hasClass("btn--grey"));
      String uriPath = buttonElements.attr("href");

      if (uriPath != null && uriPath.contains(archiveYear)) {
        return new SelectedYearDocument(ElementSelectResult.FOUND, document);
      } else {
        return new SelectedYearDocument(ElementSelectResult.NOT_FOUND, document);
      }
    };
  }
}
