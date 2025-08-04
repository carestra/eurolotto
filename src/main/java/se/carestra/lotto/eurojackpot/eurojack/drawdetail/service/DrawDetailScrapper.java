package se.carestra.lotto.eurojackpot.eurojack.drawdetail.service;

import java.util.Optional;

public interface DrawDetailScrapper {
  Optional<DrawDetails> fetchDetails(String resourceUri);
}
