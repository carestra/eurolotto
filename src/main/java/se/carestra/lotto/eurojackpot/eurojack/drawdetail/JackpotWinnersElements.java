package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import org.jsoup.nodes.Element;

import java.util.Optional;

record JackpotWinnersElements(Optional<Element> winnerElement) implements Winners {

  JackpotWinnersElements(WinnersDivElementExtractor divElements) {
    this(divElements.element);
  }

  public Boolean hasWinnersElements() {
    return winnerElement.isPresent();
  }

  public Optional<Integer> getNumberOfWinners() {
    return winnerElement
        .map(Element::text)
        .map(Integer::parseInt);
  }
}
