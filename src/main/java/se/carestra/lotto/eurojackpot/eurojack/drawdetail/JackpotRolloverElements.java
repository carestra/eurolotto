package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import org.jsoup.nodes.Element;

import java.util.Optional;

record JackpotRolloverElements(Optional<Element> ribbonSpanElement) implements RolloverRibbon {

  JackpotRolloverElements(RibbonDivElementExtractor divElements) {
    this(divElements.element);
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
