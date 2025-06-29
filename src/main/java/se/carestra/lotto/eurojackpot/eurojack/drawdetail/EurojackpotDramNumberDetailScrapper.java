package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.carestra.lotto.eurojackpot.eurojack.scrapper.JsoupScrapper;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Component
public class EurojackpotDramNumberDetailScrapper implements DrawDetailScrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(EurojackpotDramNumberDetailScrapper.class);

    private final String baseUrl;
    private final JsoupScrapper scrapper;

    public EurojackpotDramNumberDetailScrapper(@Value("${eurojackpot.url.base}") String baseUrl,
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
                    .filter(ResultElements::containsAllElements)
                    .map(mapToDrawDetails(resourceUri, fullPath));
        } catch (IOException e) {
            LOGGER.error("Could not fetch draw numbers detail from path [{}]", fullPath, e);
            return Optional.empty();
        }
    }

    private static Function<Optional<Elements>, ResultElements> extractDetails() {
        return optionalResultElements -> {
            DrawOrderBallsElements balls = new DrawOrderBallsElements(optionalResultElements);
            RibbonElements ribbon = new RibbonElements(optionalResultElements);
            WinnersElements winners = new WinnersElements(optionalResultElements);
            JackpottElements jackpottAmount = new JackpottElements(optionalResultElements);
            return new ResultElements(balls, ribbon, winners, jackpottAmount);
        };
    }

    private static Function<ResultElements, DrawDetails> mapToDrawDetails(String resourceUri, String fullPath) {
        return resultElements -> {
            Optional<EurojackpotDraw> drawNumbers = resultElements.balls().getEurojackpotDraw();
            Optional<Integer> rollover = resultElements.ribbon().getRollover();
            Optional<Integer> nrOfWinners = resultElements.winners().getNumberOfJackpotWinners();
            Optional<AmountCurrency> jackpotAmount = resultElements.jackpottAmount().getJackpotAmount();
            JackpotDetail jackpotDetail = new JackpotDetail(jackpotAmount.get(), rollover.get(), nrOfWinners.get());

            return new DrawDetails(drawNumbers.get(), jackpotDetail, resourceUri, fullPath);
        };
    }

    private static Optional<Optional<Elements>> getResultElements(Optional<Document> document) {
        return document
                .map(doc -> doc.select("div.results"))
                .filter(Objects::nonNull)
                .map(Optional::of);
    }
}
