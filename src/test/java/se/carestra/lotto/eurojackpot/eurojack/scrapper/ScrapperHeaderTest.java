package se.carestra.lotto.eurojackpot.eurojack.scrapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.carestra.lotto.eurojackpot.eurojack.scrapper.api.ScrapperHeaders;

@SpringBootTest
class ScrapperHeaderTest {

  @Autowired
  private ScrapperHeaders scrapperHeaders;

  @Test
  void shouldBeAutoWired() {
    Assertions.assertNotNull(scrapperHeaders);
  }
}
