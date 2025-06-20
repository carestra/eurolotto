package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

record TableBodyRowDataElement(Optional<Elements> tableBodyRowElements) {
  private static Optional<Stream<Element>> rowsDataStream;

  TableBodyRowDataElement {
    rowsDataStream = tableBodyRowElements
        .map(rows ->
            rows.stream()
                .map(row -> row.selectFirst("td"))
                .filter(Objects::nonNull)
        );
  }

  public Optional<Stream<Element>> getRowsDataStream() {
    return rowsDataStream;
  }
}
