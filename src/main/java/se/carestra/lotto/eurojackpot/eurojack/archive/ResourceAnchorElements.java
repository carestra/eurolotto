package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;

import java.util.Optional;
import java.util.stream.Stream;

record ResourceAnchorElements(Optional<Stream<Element>> anchorStream) implements ResourceAnchor {
  ResourceAnchorElements(ResourceAnchor.AnchorElementExtractor anchorElementExtractor) {
    this(anchorElementExtractor.anchorsStream);
  }
}
