package se.carestra.lotto.eurojackpot.eurojack.scrapper.drawdetail;

import java.util.stream.Stream;

record ResultElements(DrawOrderBallsElements balls,
                      RibbonElements ribbon,
                      WinnersElements winners,
                      JackpottElements jackpottAmount) {

  public boolean containsAllElements() {
    return Stream.of(
            balls.hasBallsListElements(),
            ribbon.hasRibbonElements(),
            winners.hasWinnersElements(),
            jackpottAmount.hasJackpotAmountElement()
        )
        .allMatch(b -> b.equals(Boolean.TRUE));
  }
}
