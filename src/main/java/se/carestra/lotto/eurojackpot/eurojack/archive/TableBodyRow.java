package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

sealed interface TableBodyRow permits TableBodyRowElements {
  class TableBodyRowExtractor {
    protected final Optional<Elements> tableBodyRowElements;

    TableBodyRowExtractor(Optional<Element> tableBodyElement) {
      this.tableBodyRowElements = tableBodyElement
          .map(table -> table.select("tr"))
          .filter(Objects::nonNull);
    }
  }
}
