package se.carestra.lotto.eurojackpot.eurojack.drawdetail;

import java.util.Optional;

public interface DrawDetailScrapper {
  Optional<DrawDetails> fetchDetails(String resourceUri);
}
