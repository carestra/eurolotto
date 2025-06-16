package se.carestra.lotto.eurojackpot.eurojack.scrapper.drawdetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

class DrawOrderBallsElements {

  private final Optional<Elements> ballsListElements;
  private final Optional<Elements> euroBallsListElements;
  private boolean missingValue = false;

  public DrawOrderBallsElements(Optional<Elements> resultsElements) {
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

    this.ballsListElements = allBallsElements
        .map(allBalls -> allBalls.select("li.ball"));

    this.euroBallsListElements = allBallsElements
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
                  .map(ballNumber -> Integer.parseInt(ballNumber))
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
                  .map(ballNumber -> Integer.parseInt(ballNumber))
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
