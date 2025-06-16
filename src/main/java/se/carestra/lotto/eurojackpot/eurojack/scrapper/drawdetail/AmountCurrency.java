package se.carestra.lotto.eurojackpot.eurojack.scrapper.drawdetail;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

record AmountCurrency(String formattedAmount, String currencySymbol) {
  private static final Locale SWEDISH_LOCALE = new Locale.Builder().setLanguage("sv").setRegion("SE").build();
  private static final NumberFormat SWEDISH_NUMBER_FORMAT = NumberFormat.getCurrencyInstance(SWEDISH_LOCALE);

  AmountCurrency {
    if (formattedAmount == null) {
      throw new IllegalArgumentException("Amount cannot be null.");
    }
    if (currencySymbol == null) {
      throw new IllegalArgumentException("Currency cannot be null.");
    }
    SWEDISH_NUMBER_FORMAT.setMaximumFractionDigits(0);
  }

  public String convertToSwedishFormat() {
    String unformattedAmount = formattedAmount.replace(",", "");
    return SWEDISH_NUMBER_FORMAT.format(new BigInteger(unformattedAmount));
  }

  public BigInteger asBigInteger() {
    return new BigInteger(formattedAmount.replace(",", ""));
  }
}
