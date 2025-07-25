package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import java.util.stream.Stream;

record DrawDetailComponents(SelectedBallsOrderedElements selectedBallsOrdered,
                            EuroBallsOrderedElements euroBallsOrdered,
                            JackpotAmountElements jackpotAmount,
                            JackpotRolloverElements jackpotRollover,
                            JackpotWinnersElements jackpotNumberOfWinners) {

  public boolean containsAllElements() {
    return Stream.of(
            selectedBallsOrdered.hasBallListElements(),
            euroBallsOrdered.hasBallListElements(),
            jackpotAmount.hasJackpotAmountElement(),
            jackpotRollover.hasRibbonElements(),
            jackpotNumberOfWinners.hasWinnersElements()
        )
        .allMatch(b -> b.equals(Boolean.TRUE));
  }
}
