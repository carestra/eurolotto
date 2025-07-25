package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

sealed interface ResourceAnchor permits ResourceAnchorElements {

  class AnchorElementExtractor {
    protected final Optional<Stream<Element>> anchorsStream;

    AnchorElementExtractor(Optional<Stream<Element>> rowsDataStream) {
      this.anchorsStream = rowsDataStream
          .map(rowStream ->
              rowStream
                  .map(row -> row.select("a"))
                  .filter(Objects::nonNull)
                  .map(anchors -> anchors.getFirst())
                  .filter(Objects::nonNull)
          );
    }
  }
}
