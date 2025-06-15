package se.carestra.lotto.eurojackpot.eurojack.scrapper.drawdetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

class JackpottElements {

  private final Optional<Element> jackpotAmountElement;

  public JackpottElements(Optional<Elements> resultsElements) {
    Optional<Elements> jackpotElements = resultsElements
        .map(results -> results.select("div.jackpot-amount"))
        .filter(Objects::nonNull);

    this.jackpotAmountElement = jackpotElements
        .map(amount -> amount.select("div.element2"))
        .filter(Objects::nonNull)
        .map(jackpotAmount -> jackpotAmount.getFirst());
  }

  public Boolean hasJackpotAmountElement() {
    return jackpotAmountElement.isPresent();
  }

  public Optional<AmountCurrency> getJackpotAmount() {
    return jackpotAmountElement
        .map(amount -> amount.text())
        .map(amountString -> {
          String currencySymbol = amountString.substring(0, amountString.indexOf("."));
          String amount = amountString.substring(amountString.indexOf(".") + 1);

          return new AmountCurrency(amount, currencySymbol);
        });
  }
}
