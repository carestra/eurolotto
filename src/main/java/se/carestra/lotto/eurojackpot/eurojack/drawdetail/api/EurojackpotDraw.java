package se.carestra.lotto.eurojackpot.eurojack.drawdetail.api;


public record EurojackpotDraw(BallNumbers ballNumbers, EuroBallNumbers euroBallNumbers) {
  public EurojackpotDraw {
    if (ballNumbers == null) {
      throw new IllegalArgumentException("drawBalls cannot be null.");
    }

    if (euroBallNumbers == null) {
      throw new IllegalArgumentException("euroBallNumbers cannot be null.");
    }
  }
}
