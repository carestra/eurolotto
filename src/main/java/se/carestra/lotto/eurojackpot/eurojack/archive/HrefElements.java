package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURI;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

record HrefElements(Optional<Stream<Element>> anchorsStream, Optional<Stream<String>> hrefsStream) {
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
                .sorted(Comparator.comparing(DrawNumberURI::drawDate, Comparator.naturalOrder()))
                .toList()
        )
        .filter(resources -> !resources.isEmpty());
  }
}
