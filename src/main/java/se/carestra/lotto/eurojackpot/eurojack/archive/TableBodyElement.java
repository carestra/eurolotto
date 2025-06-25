package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;

import java.util.Objects;
import java.util.Optional;

record TableBodyElement(Optional<Element> tableElement) {
  private static Optional<Element> tableBodyElement;

  TableBodyElement {
    tableBodyElement = tableElement
        .map(table -> table.select("tbody"))
        .filter(Objects::nonNull)
        .map(tableBody -> tableBody.getFirst());
  }

  public Boolean hasTableBody() {
    return tableBodyElement.isPresent();
  }

  public Optional<Element> getTableBody() {
    return tableBodyElement;
  }
}
