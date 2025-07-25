package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;

import java.util.Objects;
import java.util.Optional;

sealed interface TableBody permits TableBodyElement {
  class TableBodyElementExtractor {
    protected final Optional<Element> tableBodyElement;

    TableBodyElementExtractor(Optional<Element> tableElement) {
      this.tableBodyElement = tableElement
          .map(table -> table.select("tbody"))
          .filter(Objects::nonNull)
          .map(tableBody -> tableBody.getFirst());
    }
  }
}
