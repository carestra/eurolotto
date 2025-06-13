package se.carestra.lotto.eurojackpot.eurojack.scrapper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class JsoupScrapperImpl implements JsoupScrapper {
  private final ScrapperHeaders headers;

  public JsoupScrapperImpl(@Autowired ScrapperHeaders headers) {
    this.headers = headers;
  }

  @Override
  public Optional<Document> getDocument(String uri) throws IOException {
    Connection connection = Jsoup
        .connect(uri)
        .userAgent(headers.userAgent())
        .header(headers.acceptLang(), headers.acceptLangValue());

    return Optional.of(connection.get());
  }
}
