package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

sealed interface Winners permits JackpotWinnersElements {
  class WinnersDivElementExtractor {
    protected final Optional<Element> element;

    public WinnersDivElementExtractor(Optional<Elements> resultsElements) {
      Optional<Elements> winnersElements = resultsElements
          .map(results -> results.select("div.winners"))
          .filter(Objects::nonNull);

      this.element = winnersElements
          .map(ribbon -> ribbon.select("div.elem2"))
          .filter(Objects::nonNull)
          .map(winners -> winners.getFirst());
    }
  }
}
