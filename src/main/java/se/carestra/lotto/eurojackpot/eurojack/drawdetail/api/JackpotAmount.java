package se.carestra.lotto.eurojackpot.eurojack.drawdetail.api;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

public record JackpotAmount(String amount, String currencySymbol) {
  public JackpotAmount {
    if (amount == null || amount.isEmpty()) {
      throw new IllegalArgumentException("Amount cannot be null nor empty.");
    }
    if (currencySymbol == null) {
      throw new IllegalArgumentException("Currency cannot be null.");
    }
    SWEDISH_NUMBER_FORMAT.setMaximumFractionDigits(0);
    amount = amount.replace(",", "");
  }

  public JackpotAmount(BigInteger amount, String currencySymbol) {
    this(amount.toString(), currencySymbol);
  }

  private static final Locale SWEDISH_LOCALE = new Locale.Builder().setLanguage("sv").setRegion("SE").build();
  private static final NumberFormat SWEDISH_NUMBER_FORMAT = NumberFormat.getCurrencyInstance(SWEDISH_LOCALE);

  public String amountConvertToSwedishFormat() {
    return SWEDISH_NUMBER_FORMAT.format(new BigInteger(amount));
  }

  public BigInteger amountAsBigInteger() {
    return new BigInteger(amount);
  }
}
