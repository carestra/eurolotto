package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.select.Elements;

import java.util.Optional;

record TableBodyRowElements(Optional<Elements> tableBodyRowElements) implements TableBodyRow {
  TableBodyRowElements(TableBodyRowExtractor bodyRowExtractor) {
    this(bodyRowExtractor.tableBodyRowElements);
  }

  public Boolean hasTableBodyRowElements() {
    return tableBodyRowElements.isPresent();
  }
}
