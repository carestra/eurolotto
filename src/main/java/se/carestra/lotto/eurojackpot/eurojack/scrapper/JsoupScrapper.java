package se.carestra.lotto.eurojackpot.eurojack.scrapper;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Optional;

public interface JsoupScrapper {

  Optional<Document> getDocument(String uri) throws IOException;
}
