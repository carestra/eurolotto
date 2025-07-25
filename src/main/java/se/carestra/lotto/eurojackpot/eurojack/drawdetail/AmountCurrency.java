package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

sealed interface AmountCurrency permits JackpotAmountElements {

  class JackpotAmountDivElementExtractor {
    protected Optional<Element> element;
    protected boolean missingValue;

    JackpotAmountDivElementExtractor(Optional<Elements> resultsElements) {
      Optional<Elements> jackpotElements = resultsElements
          .map(results -> results.select("div.jackpot-amount"))
          .filter(Objects::nonNull);

      Optional<Elements> elements = jackpotElements
          .map(amount -> amount.select("div.element2"))
          .filter(Objects::nonNull);

      this.element = elements
          .map(jackpotAmount -> {
            Element first = jackpotAmount.first();
            if (first == null)
              missingValue = true;
            return first;
          });
    }
  }
}
