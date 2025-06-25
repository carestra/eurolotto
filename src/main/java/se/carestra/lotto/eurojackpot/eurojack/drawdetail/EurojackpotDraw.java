package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

record EurojackpotDraw(BallNumbers ballNumbers, EuroBallNumbers euroBallNumbers) {
  EurojackpotDraw {
    if (ballNumbers == null) {
      throw new IllegalArgumentException("drawBalls cannot be null.");
    }

    if (euroBallNumbers == null) {
      throw new IllegalArgumentException("euroBallNumbers cannot be null.");
    }
  }
}
