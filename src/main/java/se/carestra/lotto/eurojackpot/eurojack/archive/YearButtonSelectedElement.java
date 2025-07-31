package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

record YearButtonSelectedElement(Optional<Elements> anchorsElements) implements YearButtonAnchor {
  YearButtonSelectedElement(YearButtonElementExtractor yearButtonElementExtractor) {
    this(yearButtonElementExtractor.anchorsElements);
  }

  public boolean isSelected(String archiveYear) {
    return anchorsElements
        .map(anchorsElement -> anchorsElement.attr("href"))
        .filter(Objects::nonNull)
        .filter(href -> href.contains(archiveYear))
        .isPresent();
  }

  public Optional<String> getHref() {
    return anchorsElements
        .map(anchorsElement -> anchorsElement.attr("href"))
        .filter(Objects::nonNull);
  }
}
