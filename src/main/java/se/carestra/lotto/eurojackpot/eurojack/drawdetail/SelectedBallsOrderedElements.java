package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.service.SelectedBallNumbers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static se.carestra.lotto.eurojackpot.eurojack.drawdetail.BallsOrdered.fetchBallsElements;

record SelectedBallsOrderedElements(Optional<Elements> ballsListElements,
                                    boolean missingValue) implements BallsOrdered {
  SelectedBallsOrderedElements(UnorderBallListElementExtractor ulElements) {
    this(fetchBallsElements(ulElements, QueryBallSelector.SELECTED_BALLS), ulElements.missingValue);
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
