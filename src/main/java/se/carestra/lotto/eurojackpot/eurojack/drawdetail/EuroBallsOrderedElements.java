package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.api.EuroBallNumbers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

record EuroBallsOrderedElements(Optional<Elements> euroBallsListElements,
                                boolean missingValue) implements BallsOrdered {
  EuroBallsOrderedElements(Optional<Elements> resultsElements) {
    this(BallsOrdered.fetchBallsElements(resultsElements, QueryBallSelector.EURO_BALLS), BallsOrdered.hasMissingValue(resultsElements));
  }

  @Override
  public boolean hasBallListElements() {
    return euroBallsListElements.isPresent();
  }

  public Optional<EuroBallNumbers> getEuroBalls() {
    return euroBallsListElements
        .map(balls -> {
          List<Integer> numbers = balls.stream()
              .map(ball -> ball.select("span"))
              .filter(Objects::nonNull)
              .map(elements -> elements.getFirst())
              .map(Element::text)
              .map(Integer::parseInt)
              .toList();
          return new EuroBallNumbers(numbers);
        });
  }
}
