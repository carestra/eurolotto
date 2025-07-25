package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

sealed interface RolloverRibbon permits JackpotRolloverElements {

  class RibbonDivElementExtractor {
    protected final Optional<Element> element;

    public RibbonDivElementExtractor(Optional<Elements> resultsElements) {
      Optional<Elements> ribbonElements = resultsElements
          .map(results -> results.select("div.ribbon"))
          .filter(Objects::nonNull);

      this.element = ribbonElements
          .map(ribbon -> ribbon.select("span"))
          .filter(Objects::nonNull)
          .map(elements -> elements.getFirst());
    }
  }
}
