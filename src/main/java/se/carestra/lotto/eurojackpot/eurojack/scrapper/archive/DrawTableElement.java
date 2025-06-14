package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Element;

import java.util.Optional;

record DrawTableElement(ElementSelectResult exist, Optional<Element> optionalTableElement) {
  DrawTableElement {
    if (exist == null) {
      throw new IllegalArgumentException("Draw table not found.");
    }
  }
}
