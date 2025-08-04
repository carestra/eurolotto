package se.carestra.lotto.eurojackpot.eurojack.drawresult.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.repository.DrawDetailsRepository;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.repository.DrawResultDetail;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class DrawResultService {

  private final DrawDetailsRepository detailsRepository;

  public DrawResultService(@Autowired DrawDetailsRepository detailsRepository) {
    this.detailsRepository = detailsRepository;
  }


  @Async("repoExec-")
  public CompletableFuture<DrawDetail> fetDrawDetailForDateAsync(LocalDate drawDate) {
    return CompletableFuture.supplyAsync(() -> {
      Optional<DrawDetail> maybeDetail =
          detailsRepository
              .findByDrawDate(drawDate)
              .map(DrawResultDetail::convert)
              .map(detail ->
                  new DrawDetail(
                      detail.drawDate(),
                      detail.selectedBallNumbers().numbers(),
                      detail.euroBallNumbers().numbers(),
                      detail.jackpotAmount().amount(),
                      detail.jackpotAmount().currencySymbol(),
                      detail.jackpotRollover().rollover(),
                      detail.jackpotNumberOfWinners().nrOfWinners(),
                      detail.resourceUri(),
                      detail.fullPath()
                  ));
      return maybeDetail.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detail not found."));
    });
  }
}
