package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.api.BallNumbers;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.api.EuroBallNumbers;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.api.EurojackpotDraw;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

record DrawOrderBallsElements(Optional<Elements> resultsElements) {

  private static Optional<Elements> ballsListElements;
  private static Optional<Elements> euroBallsListElements;
  private static boolean missingValue = false;

  DrawOrderBallsElements {
    Optional<Elements> optionalElements = resultsElements
        .map(results -> results.select("ul#ballsDrawn"))
        .filter(Objects::nonNull);

    Optional<Element> allBallsElements = optionalElements
        .map(elements -> {
          Element first = elements.first();
          if (first == null)
            missingValue = true;
          return first;
        });

    ballsListElements = allBallsElements
        .map(allBalls -> allBalls.select("li.ball"));

    euroBallsListElements = allBallsElements
        .map(allBalls -> allBalls.select("li.euro"));
  }

  public Boolean hasBallsListElements() {
    return missingValue || ballsListElements.isPresent();
  }

  public Boolean hasEuroBallsListElements() {
    return euroBallsListElements.isPresent();
  }

  public Optional<EurojackpotDraw> getEurojackpotDraw() {
    Optional<BallNumbers> ballNumbers =
        ballsListElements
            .map(balls -> {
              List<Integer> numbers = balls.stream()
                  .map(ball -> ball.select("span"))
                  .filter(Objects::nonNull)
                  .map(elements -> elements.getFirst())
                  .map(Element::text)
                  .map(Integer::parseInt)
                  .toList();
              return new BallNumbers(numbers);
            });

    Optional<EuroBallNumbers> euroNumbers =
        euroBallsListElements
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

    return ballNumbers
        .flatMap(bn ->
            euroNumbers.map(en ->
                new EurojackpotDraw(bn, en)
            )
        );
  }
}
