package se.carestra.lotto.eurojackpot.eurojack.archive;

import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURI;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

record ResourceHrefElements(Optional<Stream<String>> hrefsStream) implements ResourceHref {
  ResourceHrefElements(HrefElementExtractor hrefExtractor) {
    this(hrefExtractor.hrefsStream);
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
