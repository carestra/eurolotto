package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

sealed interface BallsOrdered permits SelectedBallsOrderedElements, EuroBallsOrderedElements {

  boolean hasBallListElements();

  static boolean hasMissingValue(Optional<Elements> resultElements) {
    Optional<Elements> optionalElements = getAllBallsElements(resultElements);

    return optionalElements
        .map(elements -> {
          Element first = elements.first();
          if (first == null)
            return true;
          return false;
        })
        .orElse(true);
  }

  static Optional<Elements> fetchBallsElements(Optional<Elements> resultElements, QueryBallSelector selector) {
    Optional<Elements> optionalElements = getAllBallsElements(resultElements);

    return optionalElements
        .map(elements -> elements.first())
        .filter(Objects::nonNull)
        .map(allBalls -> allBalls.select(selector.getCssQuerySelector()));
  }

  private static Optional<Elements> getAllBallsElements(Optional<Elements> resultElements) {
    return resultElements
        .map(results -> results.select("ul#ballsDrawn"))
        .filter(Objects::nonNull);
  }

  enum QueryBallSelector {
    SELECTED_BALLS("li.ball"), EURO_BALLS("li.euro");

    private final String cssQuerySelector;

    QueryBallSelector(String cssQuerySelector) {
      this.cssQuerySelector = cssQuerySelector;
    }

    String getCssQuerySelector() {
      return this.cssQuerySelector;
    }
  }
}
