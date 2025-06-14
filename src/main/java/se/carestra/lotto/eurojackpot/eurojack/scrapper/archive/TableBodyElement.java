package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Element;

import java.util.Optional;

record TableBodyElement(ElementSelectResult exist, Optional<Element> elementOptional) {
  TableBodyElement {
    if (exist == null) {
      throw  new IllegalStateException("ElementSelectResult cannot be null.");
    }
  }
}
