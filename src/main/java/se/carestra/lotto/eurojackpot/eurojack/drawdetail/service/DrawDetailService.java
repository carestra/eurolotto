package se.carestra.lotto.eurojackpot.eurojack.drawdetail.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class DrawDetailService {
  private final DrawDetailsRepository detailsRepository;
  private final DrawDetailScrapper scrapper;

  public DrawDetailService(DrawDetailsRepository detailsRepository, DrawDetailScrapper scrapper) {
    this.detailsRepository = detailsRepository;
    this.scrapper = scrapper;
  }

  @Async()
  public CompletableFuture<Optional<DrawDetails>> findDrawDetailForDateAsync(LocalDate drawDate) {
    return CompletableFuture.supplyAsync(
        () -> detailsRepository
            .findByDrawDate(drawDate)
            .map(DrawResultDetail::convert)
            .or(() -> scrapper.fetchDetails(DrawDetails.toResourceUri(drawDate)))
    );
  }
}
