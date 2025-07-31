package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Element;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.DrawNumberURI;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

sealed interface ResourceHref permits ResourceHrefElements {

  default Optional<List<DrawNumberURI>> extractAndConvert(Optional<Stream<Element>> optionalElementStream, String fullPath) {
    return convertAndSortNaturalOrder().apply(hrefExtractor().apply(optionalElementStream), fullPath);
  }

  private BiFunction<Optional<Stream<String>>, String, Optional<List<DrawNumberURI>>> convertAndSortNaturalOrder() {
    return (hrefsStream, fullPath) ->
        hrefsStream
            .map(hrefs ->
                hrefs
                    .map(href -> new DrawNumberURI(href, fullPath))
                    .sorted(Comparator.comparing(DrawNumberURI::drawDate, Comparator.naturalOrder()))
                    .toList()
            )
            .filter(resources -> !resources.isEmpty());
  }

  private Function<Optional<Stream<Element>>, Optional<Stream<String>>> hrefExtractor() {
    return optionalElementStream -> optionalElementStream
        .map(elementStream ->
            elementStream
                .map(anchor ->
                    anchor.attr("href")
                )
                .filter(Objects::nonNull)
        );
  }
}
