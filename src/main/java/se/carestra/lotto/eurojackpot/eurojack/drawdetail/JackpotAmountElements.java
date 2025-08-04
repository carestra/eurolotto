package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import org.jsoup.nodes.Element;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.service.JackpotAmount;

import java.math.BigInteger;
import java.util.Optional;

record JackpotAmountElements(Optional<Element> jackpotAmountElement,
                             boolean missingValue) implements AmountCurrency {
  JackpotAmountElements(JackpotAmountDivElementExtractor divElements) {
    this(divElements.element, divElements.missingValue);
  }

  public Boolean hasJackpotAmountElement() {
    return missingValue || jackpotAmountElement.isPresent();
  }

  public Optional<JackpotAmount> getJackpotAmount() {
    if (missingValue) {
      return Optional.of(new JackpotAmount(BigInteger.ZERO, ""));
    }
    return jackpotAmountElement
        .map(amount -> amount.text())
        .map(amountString -> {
          String currencySymbol = amountString.substring(0, amountString.indexOf("."));
          String amount = amountString.substring(amountString.indexOf(".") + 1);

          return new JackpotAmount(amount, currencySymbol);
        });
  }
}
