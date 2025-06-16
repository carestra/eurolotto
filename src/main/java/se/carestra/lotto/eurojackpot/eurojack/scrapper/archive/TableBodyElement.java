package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Element;

import java.util.Objects;
import java.util.Optional;

public class TableBodyElement {
  private Optional<Element> tableBodyElement;
  public TableBodyElement(Optional<Element> tableElement) {
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
