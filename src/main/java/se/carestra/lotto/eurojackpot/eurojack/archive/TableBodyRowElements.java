package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

record TableBodyRowElements(Optional<Element> tableBodyElement, Optional<Elements> tableBodyRowElements) {
  TableBodyRowElements {
    tableBodyRowElements = tableBodyElement
        .map(table -> table.select("tr"))
        .filter(Objects::nonNull);
  }

  public Boolean hasTableBodyRowElements() {
    return tableBodyRowElements.isPresent();
  }
}
