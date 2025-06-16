package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

public class TableBodyRowElements {
  private Optional<Elements> tableBodyRowElements;
  public TableBodyRowElements(Optional<Element> tableBodyElement) {
    tableBodyRowElements = tableBodyElement
        .map(table -> table.select("tr"))
        .filter(Objects::nonNull);
  }

  public Boolean hasTableBodyRowElements() {
    return tableBodyRowElements.isPresent();
  }

  public Optional<Elements> getTableBodyRowElements() {
    return tableBodyRowElements;
  }
}
