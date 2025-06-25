package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

record AnchorElements(Optional<Stream<Element>> rowsDataStream)  {
  private static Optional<Stream<Element>> anchorsStream;

  AnchorElements{
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
