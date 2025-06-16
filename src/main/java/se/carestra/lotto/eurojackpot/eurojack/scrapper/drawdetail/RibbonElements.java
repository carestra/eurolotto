package se.carestra.lotto.eurojackpot.eurojack.scrapper.drawdetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

class RibbonElements {
  private final Optional<Element> ribbonSpanElement;

  public RibbonElements(Optional<Elements> resultsElements) {
    Optional<Elements> ribbonElements = resultsElements
        .map(results -> results.select("div.ribbon"))
        .filter(Objects::nonNull);

    this.ribbonSpanElement = ribbonElements
        .map(ribbon -> ribbon.select("span"))
        .filter(Objects::nonNull)
        .map(elements -> elements.getFirst());
  }

  public Boolean hasRibbonElements() {
    return ribbonSpanElement.isPresent();
  }

  public Optional<Integer> getRollover() {
    return ribbonSpanElement
        .map(Element::text)
        .map(text -> {
          if (text.matches("\\d+"))
            return text;
          else
            return text.substring(0, text.length() - 1);
        })
        .map(Integer::parseInt);
  }
}
