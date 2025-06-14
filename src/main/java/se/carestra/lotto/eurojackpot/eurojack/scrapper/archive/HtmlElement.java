package se.carestra.lotto.eurojackpot.eurojack.scrapper.archive;

import org.jsoup.nodes.Element;
import org.springframework.lang.NonNull;

import java.util.Optional;

record HtmlElement(ElementDiscovered exist, @NonNull Optional<Element> elementOptional) {
  HtmlElement {
    if (exist == null) {
      throw  new IllegalStateException("ElementDiscovered cannot be null.");
    }
  }
}
