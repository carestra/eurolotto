package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;

import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

sealed interface ResourceAnchor permits ResourceAnchorElements {

  default Optional<Stream<Element>> extract(Optional<Stream<Element>> param) {
    return extractor().apply(param);
  }

  private UnaryOperator<Optional<Stream<Element>>> extractor() {
    return rds -> rds
        .map(rowStream ->
            rowStream
                .map(row -> row.select("a"))
                .filter(Objects::nonNull)
                .map(anchors -> anchors.getFirst())
                .filter(Objects::nonNull)
        );
  }
}
