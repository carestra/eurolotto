package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Optional;
import java.util.stream.Stream;

record TableBodyRowDataElement(Optional<Stream<Element>> rowsDataStream) implements TableBodyRowData {
  TableBodyRowDataElement(TableRowDataExtractor rowDataExtractor) {
    this(rowDataExtractor.rowsDataStream);
  }
}
