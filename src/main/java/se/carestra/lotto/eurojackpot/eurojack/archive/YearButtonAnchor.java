package se.carestra.lotto.eurojackpot.eurojack.archive;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

sealed interface YearButtonAnchor permits YearButtonSelectedElement {

  class YearButtonElementExtractor {
    protected final Optional<Elements> anchorsElements;

    YearButtonElementExtractor(Optional<Document> documentElement) {
      this.anchorsElements = documentElement
          .map(document -> document.select("a.btn "))
          .filter(anchorElements -> anchorElements.hasClass("btn--grey"))
          .filter(Objects::nonNull);
    }
  }
}
