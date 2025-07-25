package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

sealed interface Table permits TableElements {
  class TableElementsExtractor {
    protected final Optional<Element> tableElements;

    TableElementsExtractor(Optional<Document> documentElement) {
      this.tableElements = documentElement
          .map(document -> document.select("table"))
          .filter(Objects::nonNull)
          .map(ArrayList::getFirst);
    }
  }
}
