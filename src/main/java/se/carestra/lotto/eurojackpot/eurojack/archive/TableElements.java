package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

record TableElements(Optional<Document> documentElement, Optional<Element> tableElements) {
  TableElements {
    tableElements = documentElement
        .map(document -> document.select("table"))
        .filter(Objects::nonNull)
        .map(ArrayList::getFirst);
  }

  public Boolean hasTableElements() {
    return tableElements.isPresent();
  }
}
