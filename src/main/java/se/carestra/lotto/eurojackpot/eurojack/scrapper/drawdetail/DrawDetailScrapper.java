package se.carestra.lotto.eurojackpot.eurojack.scrapper.drawdetail;

import java.util.Optional;

public interface DrawDetailScrapper {
  Optional<DrawDetails> fetchDetails(String resourceUri);
}
