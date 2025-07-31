package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;

import java.util.Optional;

record TableElements(Optional<Element> tableElements) implements Table {
  TableElements(TableElementsExtractor tableElementsExtractor) {
    this(tableElementsExtractor.tableElements);
  }

  public boolean hasTableElements() {
    return tableElements.isPresent();
  }
}
