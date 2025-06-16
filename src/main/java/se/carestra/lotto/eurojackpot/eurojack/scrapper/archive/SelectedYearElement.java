package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

public class SelectedYearElement {
    private final Optional<Elements> anchorsElements;

  public SelectedYearElement(Optional<Document> documentElement) {
    this.anchorsElements = documentElement
        .map(document -> document.select("a.btn "))
        .filter(anchorElements -> anchorElements.hasClass("btn--grey"))
        .filter(Objects::nonNull);
  }

  public Boolean isSelected(String archiveYear) {
    return anchorsElements
        .map( anchorsElement -> anchorsElement.attr("href"))
        .filter(Objects::nonNull)
        .filter(href -> href.contains(archiveYear))
        .isPresent();
  }

  public Optional<String> getHref() {
    return anchorsElements
        .map( anchorsElement -> anchorsElement.attr("href"))
        .filter(Objects::nonNull);
  }
}
