package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EurojackpotArchiveScrapper implements ArchiveScrapper {
  private final Logger logger = LoggerFactory.getLogger(EurojackpotArchiveScrapper.class);

  private final JsoupScrapper scrapper;

  public EurojackpotArchiveScrapper(@Autowired JsoupScrapper scrapper) {
    this.scrapper = scrapper;
  }

  @Override
  public Optional<List<DrawNumberURI>> fetch(@NonNull String baseUrl, @NonNull String archiveYearPath) {
    String fullPath = baseUrl + archiveYearPath;
    try {
      Optional<Document> document = scrapper.getDocument(fullPath);

      return findDrawTableRows(archiveYearPath, document)
          .map(mapTableRowHrefToDrawNumberURI(fullPath))
          .filter(result -> !result.isEmpty());
    } catch (IOException e) {
      logger.error("Could not fetch draw numbers detail url path from archive [{}]", fullPath, e);
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

  private Optional<Elements> findDrawTableRows(String archiveYearPath, Optional<Document> document) {
    return findDrawTableBody(document, archiveYearPath)
        .map(bodyElement -> bodyElement.select("tr"));
  }

  private Optional<Element> findDrawTableBody(Optional<Document> document, String archiveYearPath) {
    return findResultDrawTable(document, archiveYearPath)
        .map(selectHtmlElement("tbody"))
        .filter(htmlElement -> htmlElement.exist() == ElementDiscovered.FOUND)
        .flatMap(HtmlElement::elementOptional);
  }

  private Function<Element, HtmlElement> selectHtmlElement(String cssQuery) {
    return element -> {
      Elements tableElements = element.select(cssQuery);
      if (tableElements != null && !tableElements.isEmpty()) {
        return new HtmlElement(ElementDiscovered.FOUND, Optional.ofNullable(tableElements.getFirst()));
      } else {
        return new HtmlElement(ElementDiscovered.NOT_FOUND, Optional.empty());
      }
    };
  }

  private Optional<Element> findResultDrawTable(Optional<Document> document, String archiveYearPath) {
    return findDocumentForSelectedYear(document, archiveYearPath)
        .map(findTableElement("table"))
        .filter(drawTable -> drawTable.exist() == DrawTable.FOUND)
        .flatMap(ResultDrawTable::optionalTableElement);
  }

  private Function<Element, ResultDrawTable> findTableElement(String cssQuery) {
    return element -> {
      Elements tableElements = element.select(cssQuery);
      if (tableElements != null && !tableElements.isEmpty()) {
        return new ResultDrawTable(DrawTable.FOUND, Optional.ofNullable(tableElements.getFirst()));
      } else {
        return new ResultDrawTable(DrawTable.NOT_FOUND, Optional.empty());
      }
    };
  }

  private Optional<Document> findDocumentForSelectedYear(Optional<Document> document, String archiveYearPath) {
    return document
        .map(findEnabledSelectedYearButton(archiveYearPath))
        .filter(selectedYear -> selectedYear.button() == SelectedYearButton.FOUND)
        .map(SelectedYear::document);
  }

  private Function<Document, SelectedYear> findEnabledSelectedYearButton(String archiveYearPath) {
    return document -> {
      Elements buttonElements = document.select("a.btn ");
      buttonElements.removeIf(button -> button.hasClass("btn--grey"));
      String uriPath = buttonElements.attr("href");

      if (archiveYearPath.equalsIgnoreCase(uriPath)) {
        return new SelectedYear(SelectedYearButton.FOUND, document);
      } else {
        return new SelectedYear(SelectedYearButton.NOT_FOUND, document);
      }
    };
  }
}
