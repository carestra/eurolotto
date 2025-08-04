package se.carestra.lotto.eurojackpot.eurojack.drawdetail.service;

public record JackpotRollover(Integer rollover) {
  public JackpotRollover {
    if (rollover == null || rollover < 0) {
      throw new NullPointerException("rollover cannot be null, nor negative [" + rollover + "]");
    }
  }
}
