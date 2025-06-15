package se.carestra.lotto.eurojackpot.eurojack.scrapper.drawdetail;

import java.util.Optional;

interface DrawDetailScrapper {
  Optional<DrawDetails> fetchDetails(String resourceUri);
}
