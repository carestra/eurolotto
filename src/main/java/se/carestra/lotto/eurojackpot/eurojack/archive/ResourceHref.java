package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

sealed interface ResourceHref permits ResourceHrefElements {

  class HrefElementExtractor {
    protected final Optional<Stream<String>> hrefsStream;

    HrefElementExtractor(Optional<Stream<Element>> anchorsStream) {
      this.hrefsStream = anchorsStream
          .map(elementStream ->
              elementStream
                  .map(anchor ->
                      anchor.attr("href")
                  )
                  .filter(Objects::nonNull)
          );
    }
  }
}
