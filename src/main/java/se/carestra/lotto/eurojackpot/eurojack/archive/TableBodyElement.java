package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;

import java.util.Optional;

record TableBodyElement(Optional<Element> tableBodyElement) implements TableBody {
  TableBodyElement(TableBodyElementExtractor tableBodyElementExtractor) {
    this(tableBodyElementExtractor.tableBodyElement);
  }

  public Boolean hasTableBody() {
    return tableBodyElement.isPresent();
  }
}
