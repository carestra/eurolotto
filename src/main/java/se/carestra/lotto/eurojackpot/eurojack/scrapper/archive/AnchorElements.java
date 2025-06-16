package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Element;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class AnchorElements {
  private Optional<Stream<Element>> anchorsStream;

  public AnchorElements(Optional<Stream<Element>> rowsDataStream) {
    anchorsStream = rowsDataStream
        .map(rowStream ->
            rowStream
                .map(row -> row.select("a"))
                .filter(Objects::nonNull)
                .map(anchors -> anchors.getFirst())
                .filter(Objects::nonNull)
        );
  }

  public Optional<Stream<Element>> getAnchorsStream() {
    return anchorsStream;
  }
}
