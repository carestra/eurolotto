package se.carestra.lotto.eurojackpot.eurojack.scrapper.drawdetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

class JackpottElements {

  private final Optional<Element> jackpotAmountElement;
  private boolean missingValue = false;

  public JackpottElements(Optional<Elements> resultsElements) {
    Optional<Elements> jackpotElements = resultsElements
        .map(results -> results.select("div.jackpot-amount"))
        .filter(Objects::nonNull);

    Optional<Elements> elements = jackpotElements
        .map(amount -> amount.select("div.element2"))
        .filter(Objects::nonNull);

    this.jackpotAmountElement = elements
        .map(jackpotAmount -> {
          Element first = jackpotAmount.first();
          if (first == null)
            missingValue = true;
          return first;
        });
  }

  public Boolean hasJackpotAmountElement() {
    return missingValue || jackpotAmountElement.isPresent();
  }

  public Optional<AmountCurrency> getJackpotAmount() {
    if (missingValue) {
      return Optional.of(new AmountCurrency("0", "kr"));
    }
    return jackpotAmountElement
        .map(amount -> amount.text())
        .map(amountString -> {
          String currencySymbol = amountString.substring(0, amountString.indexOf("."));
          String amount = amountString.substring(amountString.indexOf(".") + 1);

          return new AmountCurrency(amount, currencySymbol);
        });
  }
}
