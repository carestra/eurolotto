package se.carestra.lotto.eurojackpot.eurojack.drawresult.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import se.carestra.lotto.eurojackpot.eurojack.archive.service.ArchiveService;
import se.carestra.lotto.eurojackpot.eurojack.drawdetail.service.DrawDetailService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("draw")
public class EurojackpotController {

  private final ArchiveService archiveService;
  private final DrawDetailService drawDetailService;

  public EurojackpotController(@Autowired ArchiveService archiveService,
                               @Autowired DrawDetailService drawDetailService) {
    this.archiveService = archiveService;
    this.drawDetailService = drawDetailService;
  }

  @GetMapping("/results/years/{year:201[2-9]|202[0-5]}")
  public CompletableFuture<Optional<List<DrawResourceURI>>> drawResourcesForYear(@PathVariable("year") String year) {
    return archiveService
        .findDrawResults(year)
        .thenApplyAsync(maybeUris ->
            maybeUris.map(uris ->
                uris.stream()
                    .map(from ->
                        new DrawResourceURI(from.drawDate(), from.resourceUri(), from.archiveUrl())
                    )
                    .toList()
            )
        )
        .whenCompleteAsync((success, failure) -> {
          if (failure != null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, failure.getMessage());
          }
          success.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        });
  }

  @GetMapping("/result/detail/date/{drawDate:(?:201[2-9]|202[0-5])-(?:0[1-9]|1[0-2])-(?:0[1-9]|[1-2][0-9]|3[0-1])}")
  public CompletableFuture<Optional<DrawDetail>> drawDetailForDate(@PathVariable("drawDate") LocalDate drawDate) {
    return drawDetailService
        .findDrawDetailForDateAsync(drawDate)
        .thenApplyAsync(maybeDetails ->
            maybeDetails.map(from ->
                new DrawDetail(
                    from.drawDate(),
                    from.selectedBallNumbers().numbers(),
                    from.euroBallNumbers().numbers(),
                    from.jackpotAmount().amount(),
                    from.jackpotAmount().currencySymbol(),
                    from.jackpotRollover().rollover(),
                    from.jackpotNumberOfWinners().nrOfWinners(),
                    from.resourceUri(),
                    from.fullPath()
                )
            )
        ).whenCompleteAsync((success, failure) -> {
          if (failure != null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, failure.getMessage());
          }
          success.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        });
  }
}
