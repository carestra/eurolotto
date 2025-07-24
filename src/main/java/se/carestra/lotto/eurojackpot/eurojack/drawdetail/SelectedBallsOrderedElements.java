package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.api.SelectedBallNumbers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

record SelectedBallsOrderedElements(Optional<Elements> ballsListElements,
                                    boolean missingValue) implements BallsOrdered {
  SelectedBallsOrderedElements(Optional<Elements> resultsElements) {
    this(BallsOrdered.fetchBallsElements(resultsElements, QueryBallSelector.SELECTED_BALLS), BallsOrdered.hasMissingValue(resultsElements));
  }

  @Override
  public boolean hasBallListElements() {
    return missingValue || ballsListElements.isPresent();
  }

  public Optional<SelectedBallNumbers> getSelectedBalls() {
    return ballsListElements
        .map(balls -> {
          List<Integer> numbers = balls.stream()
              .map(ball -> ball.select("span"))
              .filter(Objects::nonNull)
              .map(elements -> elements.getFirst())
              .map(Element::text)
              .map(Integer::parseInt)
              .toList();
          return new SelectedBallNumbers(numbers);
        });
  }
}
