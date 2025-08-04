package se.carestra.lotto.eurojackpot.eurojack.archive.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class ArchiveService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ArchiveService.class);
  private final DrawNumberURIRepository uriRepository;
  private final DrawNumberURIScrapper scrapper;
  private final Random randomGenerator = new Random();

  public ArchiveService(@Autowired DrawNumberURIRepository uriRepository,
                        @Autowired DrawNumberURIScrapper scrapper) {
    this.uriRepository = uriRepository;
    this.scrapper = scrapper;
    randomGenerator.setSeed(System.currentTimeMillis());
  }

  public CompletableFuture<Optional<List<DrawNumberURI>>> findDrawResults(String year) {
    return CompletableFuture.supplyAsync(
        () -> uriRepository
            .fetch(year)
            .or(() -> doWebScraping(year)
                .map(uris -> {
                      uriRepository.saveAll(uris);
                      LOGGER.info("Persisted {} draws for archive year {}", uris.size(), year);
                      return uris;
                    }
                )


            )
    );
  }

  private Optional<List<DrawNumberURI>> doWebScraping(String year) {
    // 1 - Rotate IP-address && Rotate user-agents headers
    //     Keep IP and Headers consistent by grouping headers and proxies logically
    // 2 - Add random delays
    //     Use exponential backoff
    //     Look at the response status code, if 429, apply exponential backoff
    // 3 - Managing session and cookies

    int randomDelay = randomGenerator.nextInt(1, 5);
    LOGGER.info("Adding random delay {} when scraping archive year {}", randomDelay, year);

    try {
      TimeUnit.SECONDS.sleep(randomDelay);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
    }

    return scrapper.fetch(year);
  }
}