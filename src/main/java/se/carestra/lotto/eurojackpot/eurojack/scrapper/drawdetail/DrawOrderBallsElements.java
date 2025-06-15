package se.carestra.lotto.eurojackpot.eurojack.scrapper.drawdetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

class DrawOrderBallsElements {

  private final Optional<Elements> ballsListElements;
  private final Optional<Elements> euroBallsListElements;

  public DrawOrderBallsElements(Optional<Elements> resultsElements) {
    Optional<Element> allBallsElements = resultsElements
        .map(results -> results.select("ul#ballsDrawn"))
        .filter(Objects::nonNull)
        .map(elements -> elements.getFirst());

    this.ballsListElements = allBallsElements
        .map(allBalls -> allBalls.select("li.ball"));

    this.euroBallsListElements = allBallsElements
        .map(allBalls -> allBalls.select("li.euro"));
  }

  public Boolean hasBallsListElements() {
    return ballsListElements.isPresent();
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
