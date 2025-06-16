package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class HrefElements {
  private Optional<Stream<String>> hrefsStream;

  public HrefElements(Optional<Stream<Element>> anchorsStream) {
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
