package se.carestra.lotto.eurojackpot.eurojack.archive.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ArchiveService {

  private final DrawNumberURIRepository uriRepository;
  private final DrawNumberURIScrapper scrapper;

  public ArchiveService(@Autowired DrawNumberURIRepository uriRepository,
                        @Autowired DrawNumberURIScrapper scrapper) {
    this.uriRepository = uriRepository;
    this.scrapper = scrapper;
  }

  @Async
  public CompletableFuture<Optional<List<DrawNumberURI>>> findDrawResults(String year) {
    return CompletableFuture.supplyAsync(() -> uriRepository.fetch(year).or(() -> scrapper.fetch(year)));
  }
}