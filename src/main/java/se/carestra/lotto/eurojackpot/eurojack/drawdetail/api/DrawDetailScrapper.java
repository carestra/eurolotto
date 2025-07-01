package se.carestra.lotto.eurojackpot.eurojack.drawdetail.api;

import java.util.Optional;

public interface DrawDetailScrapper {
  Optional<DrawDetails> fetchDetails(String resourceUri);
}
