package se.carestra.lotto.eurojackpot.eurojack.drawresult.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("draw")
public class EurojackpotController {

  private final DrawResultService drawService;

  public EurojackpotController(@Autowired DrawResultService drawService) {
    this.drawService = drawService;
  }

  @GetMapping("/results/years/{year:201[2-9]|202[0-5]}")
  public CompletableFuture<List<DrawResourceURI>> drawResourcesForYear(@PathVariable("year") String year) {
    return drawService.fetchDrawResourceURIsForYearAsync(year);
  }

  @GetMapping("/result/detail/date/{drawDate:(?:201[2-9]|202[0-5])-(?:0[1-9]|1[0-2])-(?:0[1-9]|[1-2][0-9]|3[0-1])}")
  public CompletableFuture<DrawDetail> drawDetailForDate(@PathVariable("drawDate") LocalDate drawDate) {
    // TODO: validate drawDate

    return drawService.fetDrawDetailForDateAsync(drawDate);
  }
}
