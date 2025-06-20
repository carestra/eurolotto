package se.carestra.lotto.eurojackpot.eurojack.scrapper.drawdetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

record WinnersElements(Optional<Elements> resultsElements) {
  private static Optional<Element> winnerElement;

  WinnersElements {
    Optional<Elements> winnersElements = resultsElements
        .map(results -> results.select("div.winners"))
        .filter(Objects::nonNull);

    winnerElement = winnersElements
        .map(ribbon -> ribbon.select("div.elem2"))
        .filter(Objects::nonNull)
        .map(winners -> winners.getFirst());
  }

  public Boolean hasWinnersElements() {
    return winnerElement.isPresent();
  }

  public Optional<Integer> getNumberOfJackpotWinners() {
    return winnerElement
        .map(Element::text)
        .map(Integer::parseInt);
  }
}
