package se.carestra.lotto.eurojackpot.eurojack.drawdetail.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class DrawDetailService {
  private static final Logger LOGGER = LoggerFactory.getLogger(DrawDetailService.class);
  private final DrawDetailsRepository detailsRepository;
  private final DrawDetailScrapper scrapper;
  private final Random randomGenerator = new Random();

  public DrawDetailService(DrawDetailsRepository detailsRepository, DrawDetailScrapper scrapper) {
    this.detailsRepository = detailsRepository;
    this.scrapper = scrapper;
    randomGenerator.setSeed(System.currentTimeMillis());
  }

  public CompletableFuture<Optional<DrawDetails>> findDrawDetailForDateAsync(LocalDate drawDate) {
    return CompletableFuture.supplyAsync(
        () -> detailsRepository
            .findByDrawDate(drawDate)
            .map(DrawResultDetail::convert)
            .or(() -> doWebScraping(drawDate)
                .map(detail -> {
                  detailsRepository.save(DrawResultDetail.convert(detail));
                  LOGGER.info("Persisted draw detail for date {}", drawDate);
                  return detail;
                })
            )
    );
  }

  private Optional<DrawDetails> doWebScraping(LocalDate drawDate) {
    // 1 - Rotate IP-address && Rotate user-agents headers
    //     Keep IP and Headers consistent by grouping headers and proxies logically
    // 2 - Add random delays
    //     Use exponential backoff
    //     Look at the response status code, if 429, apply exponential backoff
    // 3 - Managing session and cookies

    int randomDelay = randomGenerator.nextInt(1, 5);
    LOGGER.info("Adding delay {} when scraping draw detail for date {}", randomDelay, drawDate);

    try {
      TimeUnit.SECONDS.sleep(randomDelay);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
    }

    return scrapper.fetchDetails(DrawDetails.toResourceUri(drawDate));
  }
}
