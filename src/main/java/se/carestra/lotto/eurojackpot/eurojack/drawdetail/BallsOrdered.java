package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Objects;
import java.util.Optional;

sealed interface BallsOrdered permits SelectedBallsOrderedElements, EuroBallsOrderedElements {

  boolean hasBallListElements();

  static Optional<Elements> fetchBallsElements(UnorderBallListElementExtractor ulElements, QueryBallSelector selector) {
    return ulElements
        .allBallsElements
        .map(allBalls -> allBalls.select(selector.cssQuerySelector));
  }

  enum QueryBallSelector {
    SELECTED_BALLS("li.ball"), EURO_BALLS("li.euro");

    private final String cssQuerySelector;

    QueryBallSelector(String cssQuerySelector) {
      this.cssQuerySelector = cssQuerySelector;
    }
  }

  class UnorderBallListElementExtractor {
    private final Optional<Element> allBallsElements;
    protected boolean missingValue;

    UnorderBallListElementExtractor(Optional<Elements> resultElements) {
      Optional<Elements> optionalElements = resultElements
          .map(results -> results.select("ul#ballsDrawn"))
          .filter(Objects::nonNull);

      this.allBallsElements = optionalElements
          .map(elements -> {
            Element first = elements.first();
            if (first == null)
              this.missingValue = true;
            return first;
          });
    }
  }
}
