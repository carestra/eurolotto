package se.carestra.lotto.eurojackpot.eurojack.drawdetail.api;

public record JackpotDetail(AmountCurrency jackpotAmount, Integer rollover, Integer nrOfWinners) {
  public JackpotDetail {
    if (jackpotAmount == null) {
      throw new NullPointerException("jackpotAmount cannot be null.");
    }

    if (rollover == null || rollover < 0) {
      throw new NullPointerException("rollover cannot be null, nor negative [" + rollover + "]");
    }

    if (nrOfWinners == null || nrOfWinners < 0) {
      throw new NullPointerException("nrOfWinners cannot be null, nor negative [" + nrOfWinners + "]");
    }
  }
}
