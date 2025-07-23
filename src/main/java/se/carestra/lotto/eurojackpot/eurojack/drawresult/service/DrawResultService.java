package se.carestra.lotto.eurojackpot.eurojack.drawresult.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.carestra.lotto.eurojackpot.eurojack.archive.repository.DrawNumberURIRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class DrawResultService {

  private final DrawNumberURIRepository repository;

  public DrawResultService(@Autowired DrawNumberURIRepository drawNumberURIRepository) {
    this.repository = drawNumberURIRepository;
  }

  @Async("postgresql-io")
  public CompletableFuture<List<DrawResourceURI>> fetchDrawResourceURIsForYearAsync(String year) {
    return CompletableFuture.supplyAsync(() -> {
      Optional<List<DrawResourceURI>> maybeUris =
          repository
              .fetch(year)
              .map(result ->
                  result.stream()
                      .map(from ->
                          new DrawResourceURI(from.drawDate(), from.resourceUri(), from.archiveUrl())
                      )
                      .toList()
              );

      return maybeUris.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Archive not found."));
    });
  }

}
