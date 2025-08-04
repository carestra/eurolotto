package se.carestra.lotto.eurojackpot.eurojack.drawdetail.service;

public record JackpotNumberOfWinners(Integer nrOfWinners) {
  public JackpotNumberOfWinners {
    if (nrOfWinners == null || nrOfWinners < 0) {
      throw new NullPointerException("nrOfWinners cannot be null, nor negative [" + nrOfWinners + "]");
    }
  }
}
