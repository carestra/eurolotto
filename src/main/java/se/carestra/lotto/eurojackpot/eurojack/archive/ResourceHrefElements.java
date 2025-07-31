package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURI;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

record ResourceHrefElements(ResourceAnchorElements anchors) implements ResourceHref {

  ResourceHrefElements(Optional<Stream<Element>> anchorStream) {
    this(new ResourceAnchorElements(anchorStream));
  }

  public Optional<List<DrawNumberURI>> getDrawNumberURIs(String fullPath) {
    return extractAndConvert(anchors.anchorStream(), fullPath);
  }
}
