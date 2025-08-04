package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.service.*;
import se.carestra.lotto.eurojackpot.eurojack.scrapper.api.JsoupScrapper;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Component
class EurojackpotDramNumberDetailScrapper implements DrawDetailScrapper {
  private static final Logger LOGGER = LoggerFactory.getLogger(EurojackpotDramNumberDetailScrapper.class);

  private final String baseUrl;
  private final JsoupScrapper scrapper;

  EurojackpotDramNumberDetailScrapper(@Value("${eurojackpot.url.base}") String baseUrl,
                                      @Autowired JsoupScrapper scrapper) {
    this.scrapper = scrapper;
    this.baseUrl = baseUrl;
  }

  @Override
  public Optional<DrawDetails> fetchDetails(String resourceUri) {
    String fullPath = baseUrl + resourceUri;
    try {
      Optional<Document> document = scrapper.getDocument(fullPath);

      LOGGER.info("Fetching resource document: {}", resourceUri);
      return getResultElements(document)
          .map(extractDetails())
          .filter(DrawDetailComponents::containsAllElements)
          .flatMap(mapToDrawDetails(resourceUri, fullPath));
    } catch (IOException e) {
      LOGGER.error("Could not fetch draw numbers detail from path [{}]", fullPath, e);
      return Optional.empty();
    }
  }

  private static Function<Optional<Elements>, DrawDetailComponents> extractDetails() {
    return optionalResultElements -> {
      BallsOrdered.UnorderBallListElementExtractor unorderBallListElementExtractor = new BallsOrdered.UnorderBallListElementExtractor(optionalResultElements);
      SelectedBallsOrderedElements selectedBallsOrdered = new SelectedBallsOrderedElements(unorderBallListElementExtractor);
      EuroBallsOrderedElements euroBallsOrdered = new EuroBallsOrderedElements(unorderBallListElementExtractor);
      JackpotAmountElements jackpotAmount = new JackpotAmountElements(new AmountCurrency.JackpotAmountDivElementExtractor(optionalResultElements));
      JackpotRolloverElements ribbon = new JackpotRolloverElements(new RolloverRibbon.RibbonDivElementExtractor(optionalResultElements));
      JackpotWinnersElements winners = new JackpotWinnersElements(new Winners.WinnersDivElementExtractor(optionalResultElements));

      return new DrawDetailComponents(selectedBallsOrdered, euroBallsOrdered, jackpotAmount, ribbon, winners);
    };
  }

  private static Function<DrawDetailComponents, Optional<DrawDetails>> mapToDrawDetails(String resourceUri, String fullPath) {
    return resultElements -> {
      Optional<SelectedBallNumbers> selectedBallNumbers = resultElements.selectedBallsOrdered().getSelectedBalls();
      Optional<EuroBallNumbers> euroBallNumbers = resultElements.euroBallsOrdered().getEuroBalls();
      Optional<JackpotAmount> jackpotAmount = resultElements.jackpotAmount().getJackpotAmount();
      Optional<JackpotRollover> rollover = resultElements.jackpotRollover().getRollover().map(JackpotRollover::new);
      Optional<JackpotNumberOfWinners> jackpotNumberOfWinners = resultElements.jackpotNumberOfWinners().getNumberOfWinners().map(JackpotNumberOfWinners::new);

      return selectedBallNumbers
          .flatMap(selectedBalls ->
              euroBallNumbers
                  .flatMap(euroBalls ->
                      jackpotAmount
                          .flatMap(amount ->
                              rollover
                                  .flatMap(rolloverCount ->
                                      jackpotNumberOfWinners
                                          .map(numberOfWinners ->
                                              new DrawDetails(selectedBalls, euroBalls, amount, rolloverCount, numberOfWinners, resourceUri, fullPath)
                                          )
                                  )
                          )
                  )
          );
    };
  }

  private static Optional<Optional<Elements>> getResultElements(Optional<Document> document) {
    return document
        .map(doc -> doc.select("div.results"))
        .filter(Objects::nonNull)
        .map(Optional::of);
  }
}
