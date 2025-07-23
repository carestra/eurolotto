package se.carestra.lotto.eurojackpot.eurojack.drawresult.service;

import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.carestra.lotto.eurojackpot.eurojack.archive.api.FetchArchiveEvent;

@RestController
@RequestMapping("admin")
public class DrawFetchArchiveAdminController {

  private final ApplicationEventPublisher publisher;

  public DrawFetchArchiveAdminController(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Transactional
  @GetMapping("/trigger/archive/year/{year:^201[2-9]|202[0-5]}")
  public void triggerFetchArchive(@PathVariable("year") String year) {
    publisher.publishEvent(new FetchArchiveEvent(year));
  }
}
