package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

record HrefElements(Optional<Stream<Element>> anchorsStream) {
  private static Optional<Stream<String>> hrefsStream;

  HrefElements {
    hrefsStream = anchorsStream
        .map(elementStream ->
            elementStream
                .map(anchor ->
                    anchor.attr("href")
                )
                .filter(Objects::nonNull)
        );
  }

  public Optional<List<DrawNumberURI>> getDrawNumberURIs(String fullPath) {
    return hrefsStream
        .map(hrefs ->
            hrefs
                .map(href -> new DrawNumberURI(href, fullPath))
                .toList()
        )
        .filter(resources -> !resources.isEmpty());
  }
}
