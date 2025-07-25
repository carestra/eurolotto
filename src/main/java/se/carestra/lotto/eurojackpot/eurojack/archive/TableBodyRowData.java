package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

sealed interface TableBodyRowData permits TableBodyRowDataElement {

  class TableRowDataExtractor {
    protected final Optional<Stream<Element>> rowsDataStream;

    TableRowDataExtractor(Optional<Elements> tableBodyRowElements) {
      this.rowsDataStream = tableBodyRowElements
          .map(rows ->
              rows.stream()
                  .map(row -> row.selectFirst("td"))
                  .filter(Objects::nonNull)
          );
    }
  }
}
